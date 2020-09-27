/*
 * *****************************************************************************
 *  Copyright (C) 2014-2020 Dennis Sheirer
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
 * ****************************************************************************
 */

package io.github.dsheirer.controller.channel;

/**
 * Notification that a channel configuration has changed.
 */
public class ChannelConfigurationChangeNotification
{
    private Channel mChannel;

    /**
     * Constructs an instance
     * @param channel current
     */
    public ChannelConfigurationChangeNotification(Channel channel)
    {
        mChannel = channel;
    }

    /**
     * New channel configuration
     */
    public Channel getChannel()
    {
        return mChannel;
    }
}
