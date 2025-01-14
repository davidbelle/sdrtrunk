/*
 * ******************************************************************************
 * sdrtrunk
 * Copyright (C) 2014-2019 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * *****************************************************************************
 */

package io.github.dsheirer.preference.swing;

import io.github.dsheirer.preference.UserPreferences;
import io.github.dsheirer.util.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.Integer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Monitors a JTable column model and persists column width changes to the user preferences.  Restores
 * previous column width preferred sizes on application restart.
 */
public class JTableColumnWidthMonitor
{
    private static final Logger mLog = LoggerFactory.getLogger(JTableColumnWidthMonitor.class);
    private UserPreferences mUserPreferences;
    private JTable mTable;
    private String mKey;
    private ColumnResizeListener mColumnResizeListener = new ColumnResizeListener();
    private AtomicBoolean mSaveInProgress = new AtomicBoolean();

    /**
     * Constructs a column width monitor.
     *
     * @param userPreferences to store column widths
     * @param table to monitor for column width changes
     * @param key that uniquely identifies the table to monitor
     */
    public JTableColumnWidthMonitor(UserPreferences userPreferences, JTable table, String key)
    {
        mUserPreferences = userPreferences;
        mTable = table;
        mKey = key;

        restoreColumnWidths();
        mTable.getColumnModel().addColumnModelListener(mColumnResizeListener);
    }

    /**
     * Prepares this monitor for disposal by unregistering as a listener to the table column model.
     */
    public void dispose()
    {
        if(mTable != null && mColumnResizeListener != null)
        {
            mTable.getColumnModel().removeColumnModelListener(mColumnResizeListener);
        }

        mTable = null;
        mUserPreferences = null;
    }

    /**
     * Sets the preferred column widths on the table from persisted settings
     */
    private void restoreColumnWidths()
    {
        TableColumnModel model = mTable.getColumnModel();

        for(int x = 0; x < model.getColumnCount(); x++)
        {
            int width = mUserPreferences.getSwingPreference().getInt(getColumnKey(model.getColumn(x).getModelIndex()), Integer.MAX_VALUE);

            if(width != Integer.MAX_VALUE)
            {
                model.getColumn(x).setPreferredWidth(width);
            }
        }


        restoreColumnPositions();
    }

    /**
     * Stores the current column widths to the user preferences
     */
    private void storeColumnWidths()
    {
        TableColumnModel model = mTable.getColumnModel();
        for(int x = 0; x < model.getColumnCount(); x++)
        {
            mUserPreferences.getSwingPreference().setInt(getColumnKey(model.getColumn(x).getModelIndex()), model.getColumn(x).getWidth());
        }
    }

    /**
     * Stores the current column widths to the user preferences
     */
    private void storeColumnPositions()
    {
        TableColumnModel model = mTable.getColumnModel();
        for(int x = 0; x < model.getColumnCount(); x++)
        {
            mUserPreferences.getSwingPreference().setInt(getColumnKeyPosition(model.getColumn(x).getModelIndex()), x);
            // overwrite the widths too since the position has changed
            mUserPreferences.getSwingPreference().setInt(getColumnKey(model.getColumn(x).getModelIndex()), model.getColumn(x).getWidth());
        }
    }
    /**
     * Sets the preferred column widths on the table from persisted settings
     */
    private void restoreColumnPositions()
    {
        TableColumnModel model = mTable.getColumnModel();
        HashMap<Integer,TableColumn> newOrder = new HashMap<Integer,TableColumn>(model.getColumnCount());

        for(int x =  model.getColumnCount()-1; x >= 0; x--)
        {
            int position = mUserPreferences.getSwingPreference().getInt(getColumnKeyPosition(model.getColumn(x).getModelIndex()), Integer.MAX_VALUE);
            if(position != Integer.MAX_VALUE)
            {
                TableColumn col = model.getColumn(x);
                newOrder.put(position,col);
                model.removeColumn(col);
            }
        }

        for ( Map.Entry<Integer, TableColumn> entry : newOrder.entrySet() ) {
            Integer key = entry.getKey();
            TableColumn value = entry.getValue();
            mTable.addColumn(value);
        }
    }

    /**
     * Constructs a preference key for the column number (width)
     */
    private String getColumnKey(int column)
    {
        return mKey + ".column." + column;
    }

    /**
     * Constructs a preference key for the column number (position)
     */
    private String getColumnKeyPosition(int column)
    {
        return mKey + ".columnposition." + column;
    }

    /**
     * Table column model listener.
     */
    class ColumnResizeListener implements TableColumnModelListener
    {
        @Override
        public void columnMarginChanged(ChangeEvent e)
        {
            if(mSaveInProgress.compareAndSet(false, true))
            {
                ThreadPool.SCHEDULED.schedule(new ColumnWidthSaveTask(), 2, TimeUnit.SECONDS);
            }
        }

        @Override
        public void columnAdded(TableColumnModelEvent e){}
        @Override
        public void columnRemoved(TableColumnModelEvent e){}
        @Override
        public void columnMoved(TableColumnModelEvent e){
            if(mSaveInProgress.compareAndSet(false, true))
            {
                ThreadPool.SCHEDULED.schedule(new ColumnWidthMoveTask(), 2, TimeUnit.SECONDS);
            }
        }
        @Override
        public void columnSelectionChanged(ListSelectionEvent e){}
    }

    public class ColumnWidthSaveTask implements Runnable
    {

        @Override
        public void run()
        {
            storeColumnWidths();
            mSaveInProgress.set(false);
        }
    }

    public class ColumnWidthMoveTask implements Runnable
    {
        @Override
        public void run()
        {
            mLog.debug("saving positions and widths");
            storeColumnPositions();
            mSaveInProgress.set(false);
        }
    }
}
