// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Libraries.Speedmapper;

public class DriveTrain extends SubsystemBase {
  public CANSparkMax left1, left2, right1, right2;
  public CANEncoder leftE1, leftE2, rightE1, rightE2;
  public Speedmapper mapLeft1, mapLeft2, mapRight1, mapRight2;
  public Boolean arcDBool = true;
  public Double fr = 0.6, tr = 0.4, fullr = 0.5;
  public int countsPerRev = 42, PtoEScale = 5676;

  /** A new method of creating a drive train that
   * will allow more fluid controls and better
   * automonous controlled parts during the teleop
   * period. Will be designed to take in sensor
   * inputs and allow for user control to be negated
   * in an efficent and easy manner.
   * @apiNote Call in robotcontainer and nowhere else.
   * 
   * @param forwardRate the scale at which the robot
   * moves forward.
   * @param twistRate the scale at which the robot will
   * turn.
   */
  public DriveTrain(Double forwardRate, Double twistRate, Double fullRate) {
    fr = forwardRate;
    tr = twistRate;
    fullr = fullRate;

    left1 = new CANSparkMax(0, MotorType.kBrushless);
    left2 = new CANSparkMax(1, MotorType.kBrushless);
    right1 = new CANSparkMax(2, MotorType.kBrushless);
    right2 = new CANSparkMax(3, MotorType.kBrushless);

    mapLeft1 = new Speedmapper(5676, 0.1, 1);
    mapLeft2 = new Speedmapper(5676, 0.1, 1);
    mapRight1 = new Speedmapper(5676, 0.1, 1);
    mapRight2 = new Speedmapper(5676, 0.1, 1);

    leftE1 = left1.getEncoder(EncoderType.kHallSensor, countsPerRev);
    leftE2 = left2.getEncoder(EncoderType.kHallSensor, countsPerRev);
    rightE1 = right1.getEncoder(EncoderType.kHallSensor, countsPerRev);
    rightE2 = right2.getEncoder(EncoderType.kHallSensor, countsPerRev);

    left1.setOpenLoopRampRate(1);
    left2.setOpenLoopRampRate(1);
    right1.setOpenLoopRampRate(1);
    right2.setOpenLoopRampRate(1);
  }

  @Override
  public void periodic() {}

  /**
   * Arcade drive refers to a tank drive train robot being controlled
   * by a single joystick, much like an arcade game.
   * @param joystickY The virtical tilt of the joystick
   * @param joystickZ The twist of the joystick
   * @param UseSpeedMap if each motor should be matched. Useful for sensitive
   * motor control systems.
   * @param Interruptable if this module is allowed to be negated by other
   * processes
   * @param Priority The priority this will run in, will determine what other commands
   * to negate
   */
  public void arcadeDrive(Double joystickY, Double  joystickZ, Boolean UseSpeedMap, Boolean Interruptable, int Priority){
    if (arcDBool){
      if (UseSpeedMap){
        left1.set(mapLeft1.map(leftE1.getVelocity(), joystickY, joystickZ, fr, tr));
        left2.set(mapLeft2.map(leftE2.getVelocity(), joystickY, joystickZ, fr, tr));
        right1.set(mapRight1.map(rightE1.getVelocity(), joystickY, joystickZ, fr, tr));
        right2.set(mapRight2.map(rightE2.getVelocity(), joystickY, joystickZ, fr, tr));
      }else{
        left1.set ((joystickY * fr + joystickZ * tr)/ fullr);
        left2.set ((joystickY * fr - joystickZ * tr)/ fullr);
        right1.set((joystickY * fr + joystickZ * tr)/ fullr);
        right2.set((joystickY * fr - joystickZ * tr)/ fullr);
      }
    }
  }

  //Override methods
    public void arcadeDrive(Double joystickY, Double  joystickZ, Boolean Interruptable, int Priority){
      arcadeDrive(joystickY, joystickZ, false , Interruptable, Priority);}
    public void arcadeDrive(Double joystickY, Double  joystickZ, Boolean UseSpeedMap){
      arcadeDrive(joystickY, joystickZ, UseSpeedMap, false , 10);}
    public void arcadeDrive(Double joystickY, Double joystickZ){
      arcadeDrive(joystickY, joystickZ, false, false, 10);}
  //

  /**
   * Sets the scale at which the robot applies its
   * power to the robot. Use a scale from 0 - 1 and
   * 1 being 100%.
   * @param rate percentage of power to apply wtih 1 being 100%
   */
  public void setForwardRate(double rate){fr = rate;}
  /**
   * Sets the scale at which the robot applies its
   * power to the robot. Use a scale from 0 - 1 and
   * 1 being 100%.
   * @param rate percentage of power to apply wtih 1 being 100%
   */
  public void setTurnRate(double rate){tr = rate;}

  /**
   * Sets the scale of the full motors according to a 
   * rate of 0 - 1 as 1 being 100%.
   * @param rate percentage of power to apply wtih 1 being 100%
   */
  public void setFullRate(double rate){fullr = rate;}
}
