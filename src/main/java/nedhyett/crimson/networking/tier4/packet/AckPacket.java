/*
 * The MIT License
 *
 * Copyright 2017 Ned Hyett.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package nedhyett.crimson.networking.tier4.packet;

import nedhyett.crimson.networking.tier4.*;

/**
 * (Created on 22/09/2016)
 *
 * @author ned
 */
public class AckPacket extends Tier4Packet {

	public String ackID;

	public AckPacket() {

	}

	public AckPacket(String ackID) {
		this.ackID = ackID;
	}

	protected void execute(IPipeline pipeline) {
		pipeline.publishAckEvent(ackID, this);
	}

	@Override
	public void processPacketServer(Tier4Client c, Tier4ServerPipeline p) {
		execute(p);
	}

	@Override
	public void processPacketClient(Tier4ClientPipeline p) {
		execute(p);
	}

	@Override
	public boolean canProcessOnSide(Side side) {
		return true;
	}

}