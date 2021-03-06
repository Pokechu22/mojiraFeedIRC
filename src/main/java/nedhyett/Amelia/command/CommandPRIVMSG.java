/*
 * Copyright (c) 2014, Ned Hyett
 *  All rights reserved.
 * 
 *  By using this program/package/library you agree to be completely and unconditionally
 *  bound by the agreement displayed below. Any deviation from this agreement will not
 *  be tolerated.
 * 
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 * 
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice, this
 *  list of conditions and the following disclaimer in the documentation and/or other
 *  materials provided with the distribution.
 *  3. The redistribution is not sold, unless permission is granted from the copyright holder.
 *  4. The redistribution must contain reference to the original author, and this page.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package nedhyett.Amelia.command;

import nedhyett.Amelia.*;
import nedhyett.Amelia.core.users.User;
import nedhyett.Amelia.enums.Replies;
import nedhyett.Amelia.managers.ChannelManager;
import nedhyett.Amelia.managers.UserManager;

/**
 * Handles a PRIVMSG command.
 *
 * @author Ned
 */
public class CommandPRIVMSG implements ICommand {

    @Override
    public void exec(User user, String[] args, String rawmsg) {
	String name = args[0];
	String message;
	if (!rawmsg.contains(":")) {
	    message = args[1];
	} else {
	    message = rawmsg.substring(rawmsg.indexOf(':') + 1);
	}
	if (!ChannelManager.validateChannelName(name)) {
	    User u = UserManager.getUser(name);
	    if (u == null) {
		user.sendRawS(Replies.ERR_NOSUCHNICK.format(user.nick, name));
		return;
	    }
	    u.sendMessage(user.getID(), message);
	    user.lastAction = System.currentTimeMillis();
	    return;
	}

	if (!ChannelManager.channelExists(name)) {
	    user.sendRawS(Replies.ERR_NOSUCHCHANNEL.format(user.nick, name));
	    return;
	}
	Channel channel = ChannelManager.getChannel(name);
	if (channel.noexternal && !ChannelManager.getAllChannelsWithUser(user).contains(channel)) {
	    user.sendRawS(Replies.ERR_NOTONCHANNEL.format(user.nick, channel.name));
	    return;
	}
	if (!channel.canSpeak(user)) {
	    user.sendRawS(Replies.ERR_CANNOTSENDTOCHAN.format(user.nick, channel.name));
	    return;
	}
	user.lastAction = System.currentTimeMillis();
	channel.sendMsg(message, UserManager.getUser(user.nick));
    }

}
