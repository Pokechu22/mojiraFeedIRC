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

package nedhyett.crimson.utility.mouse;

import nedhyett.crimson.utility.GenericUtils;
import nedhyett.crimson.eventreactor.EventReactor;

import java.awt.*;

/**
 * (Created on 14/02/2015)
 *
 * @author Ned Hyett
 */
public class MouseMovementListener extends Thread {

	private final EventReactor reactor;
	private Point lastPoint = null;

	public static MouseMovementListener startLooking(EventReactor reactor) {
		MouseMovementListener mmlt = new MouseMovementListener(reactor);
		mmlt.setDaemon(true);
		mmlt.start();
		return mmlt;
	}

	MouseMovementListener(EventReactor reactor) {
		this.reactor = reactor;
	}

	@Override
	public void run() {
		try {
			while(!this.isInterrupted()) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				if(!p.equals(lastPoint)) reactor.publish(new MouseMovedEvent(lastPoint, p));
				lastPoint = p;
				GenericUtils.wait(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
