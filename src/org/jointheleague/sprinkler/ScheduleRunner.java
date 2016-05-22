package org.jointheleague.sprinkler;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.gpio.*;

/**
 * A ScheduleRunner instance executes repeatedly the list of actions that is
 * passed to it in its constructor.
 * 
 * @author ecolban
 * 
 */
public class ScheduleRunner extends Thread {

	private List<GpioAction> actions;
	private final static GpioController gpio = GpioFactory.getInstance();
	// Output pins
	private final static GpioPinDigitalOutput[] myLeds = getGPIOs() ;

	private static final Logger logger = Logger
			.getLogger(SprinklerController.class.getName());

	public ScheduleRunner(List<GpioAction> actions) {
		this.actions = actions;
	}

	/**
	 * Executes a list of GpioAction's repeatedly. Exits gracefully if
	 * interrupted. The list of actions must be sorted by time and the first
	 * must have a time later than now. The list must not contain more than one
	 * week's worth of actions. After executing an action, the time of the
	 * action is incremented by one week so it can be executed again a week
	 * later.
	 * 
	 * @param actions
	 *            a list of GpioAction's
	 */
	@Override
	public void run() {
		try {
			while (true) {
				for (GpioAction action : actions) {
					long sleepTime = action.getTimeOfAction()
							- TestTime.currentTimeMillis();
					if (0L < sleepTime) {
//						logger.log(Level.INFO, "Going to sleep for {0}ms",
//								sleepTime);
						Thread.sleep(sleepTime / TestTime.TIME_FACTOR);
					}
					Set<Integer> head = action.getHeadsOff();
					for (Integer i: head) {
						 myLeds[i.intValue()].setState(PinState.HIGH);
					}
					head = action.getHeadsOn();
					for (Integer i: head) {
						 myLeds[i.intValue()].setState(PinState.LOW);
					}
//					logger.log(Level.INFO, "{0}: {1}", new Object[] {
//							new Date(TestTime.currentTimeMillis()), action });
					action.addWeek();
				}
			}
		} catch (InterruptedException ex) {
			logger.log(Level.INFO, "Interrupted on: {0}",
					new Date(TestTime.currentTimeMillis()));

		} finally {
			// Set all pins to "off"
			for (int i = 0; i < myLeds.length; i++) {
				 myLeds[i].setState(PinState.HIGH);
			}
			logger.info("Turning all heads off.");
			gpio.shutdown();
		}
	}

	/**
	 * Called to exit gracefully. This method blocks until this thread is dead.
	 * 
	 * @throws InterruptedException
	 *             if interrupted while waiting for this runner to die.
	 */
	public void exitGracefully() throws InterruptedException {
		if (isAlive()) {
			interrupt();
			join();
		}
	}

	/**
	 * Sets up the output pins.
	 * 
	 * @return a GpioGateway instance
	 */
	private static GpioPinDigitalOutput [] getGPIOs() {
		GpioPinDigitalOutput[] myLeds = new GpioPinDigitalOutput[8];
		myLeds[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.HIGH);
        myLeds[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.HIGH);
        myLeds[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.HIGH);
        myLeds[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.HIGH);
        myLeds[4] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.HIGH);
        myLeds[5] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.HIGH);
        myLeds[6] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.HIGH);
        myLeds[7] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.HIGH);
        
        return myLeds;
	}

}
