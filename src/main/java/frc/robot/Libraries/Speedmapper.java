package frc.robot.Libraries;

public class Speedmapper {
    public int pte = 5676, tolerance_ = 0;
    public double count = 0, delta_ = 0.1;
    public Speedmapper(int PtoEScale, Double delta, int tolerance){
        pte = PtoEScale;
        count = 0; // Count starts always at 0
        delta_ = delta;
        tolerance_ = tolerance;

    }

    /**
     * Uses encoders to scale values of motors to all match, usefull
     * when motor values have to be exact
     * @requires Encoders on each drivetrain motors
     * @param encoderval the encoder value of the val motor
     * @param JoystickF forward joystick used to create a target to match the value to
     * @param JoystickT twist joystick used to create a target to match the value to
     * @param Frate the forward rate, generally equal to forwardRate.
     * @param Trate the twist rate, generally equal to twistRate.
     * @param tolerance the allowed difference between the val and joystick.
     */
    public double map(Double encoderval, Double JoystickF, Double JoystickT, Double Frate, Double Trate){
        Double target = (JoystickF * Frate + JoystickT * Trate) * pte;

        if (encoderval < target - tolerance_){
            count = count + delta_;
        }
        else if (encoderval > target + tolerance_){
            count = count - delta_;
        }

        return (JoystickF * Frate + JoystickT * Trate) + count;
    }
    
}
