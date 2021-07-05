// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArcadeDriveTrain extends SubsystemBase {

  // Initialize drive instances here
  public drive mainDrive = new drive(10, 30, true);
  public drive subDrive = new drive(4, 20, true);
  public drive reactionDrive = new drive(5, 40, false);

  public static Double fr, tr, fullr;
  public static CANSparkMax left1, left2, right1, right2;
  public static CANEncoder leftE1, leftE2, rightE1, rightE2;
  public Speedmapper mapLeft1, mapLeft2, mapRight1, mapRight2;
  
  public int countsPerRev = 42, PtoEScale = 5676;

  public static Double pubJoystickY, pubJoystickZ;

  /** A new method of creating a drive train that
   * will allow more fluid controls and better
   * automonous controlled parts during the teleop
   * period. Will be designed to take in sensor
   * inputs and allow for user control to be negated
   * in an efficent and easy manner.
   * @apiNote Call in robotcontainer and nowhere else.
   * 
   * @param forwardRate the scale at which the robot
   * moves forward. Recomended ≈ 0.6
   * @param twistRate the scale at which the robot will
   * turn. Recomended ≈ 0.4
   * @param fullRate the full scale of the robots
   * movements. Recomended ≈ 0.5
   */
  public ArcadeDriveTrain(Double forwardRate, Double twistRate, Double fullRate) {
    fr = forwardRate;
    tr = twistRate;
    fullr = fullRate;

    left1 = new CANSparkMax(0, MotorType.kBrushless);
    left2 = new CANSparkMax(1, MotorType.kBrushless);
    right1 = new CANSparkMax(2, MotorType.kBrushless);
    right2 = new CANSparkMax(3, MotorType.kBrushless);

    mapLeft1 = new Speedmapper(PtoEScale, 0.1, 1);
    mapLeft2 = new Speedmapper(PtoEScale, 0.1, 1);
    mapRight1 = new Speedmapper(PtoEScale, 0.1, 1);
    mapRight2 = new Speedmapper(PtoEScale, 0.1, 1);

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
  public void periodic() {

    Double[] s_ = {scale("left1"), scale("left2"), scale("right1"), scale("right2")}; // Scaled to input settings
    Double[] r_ = {getResistance(leftE1, s_[0]), getResistance(leftE2, s_[1]), getResistance(rightE1, s_[2]), getResistance(rightE2, s_[3])}; // Altered to resistance

    Double encoderAverage = r_[0] + r_[1] + r_[2] + r_[3] / 4; // Collect average and round to int

    // Priority changes should be mentioned here
    reactionDrive.setPriority((int) Math.round(encoderAverage));
    // Each drive instance should be mentioned here
    mainDrive.periodic(subDrive, reactionDrive);
    subDrive.periodic(mainDrive, reactionDrive);
    reactionDrive.periodic(mainDrive, subDrive);
    // Drive updates should be mentioned here
    reactionDrive.mandrive(r_[0], r_[1], r_[2], r_[3]);

    SmartDashboard.putNumber("mainDrive Prio", mainDrive.pr);
    
  }

  /**
   * Should be default (For joystick value reasons)
   * Drives the robot using Y (tilt up and down) and Z (rotation left and right)
   * @param joystickY tilt up and down
   * @param joystickZ tilt left and right
   * @param useSpeedMap if it should match all values of each motor to a target
   */
  public void arcadeDrive(Double joystickY, Double joystickZ, Boolean useSpeedMap){
    pubJoystickY = joystickY; // Grabs inputs regardless of priorities
    pubJoystickZ = joystickZ;

    if (useSpeedMap){mainDrive.arcdrive(joystickY, joystickZ, mapLeft1, mapLeft2, mapRight1, mapRight2);}
    else{mainDrive.arcdrive(joystickY, joystickZ);}
  }

  /**
   * Will drive the robot using raw inputs into the motors
   * @param leftA left1
   * @param leftB left2
   * @param rightA right1
   * @param rightB right2
   */
  public static void drive(Double leftA, Double leftB, Double rightA, Double rightB){
    left1.set(leftA);
    left2.set(leftB);
    right1.set(rightA);
    right2.set(rightB);
  }

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

  /**
   * Gets the applied power and compares it to the actual speed of the motors
   * @param encoder the encoder instance
   * @param applied the power being applied to the robot
   * @return returns the offset of expected power and applied power
   */
  public Double getResistance(CANEncoder encoder, Double applied){
    // motor should always be zero unless priority is set higher
    Double actualToPowerOffset = applied - encoder.getVelocity()/PtoEScale;
    return applied + actualToPowerOffset;
  }

  /**
   * Scales the raw power to the settings defined.
   * @param motorPosition left1, left2, right1, right2
   * @return Scaled power val to be used by motors
   */
  public static Double scale(String motorPosition){
    int factor = 1;
    if (motorPosition == "left2"){factor = -1;}
    if (motorPosition == "right2"){factor = -1;}
    
    return (pubJoystickY * fr + (pubJoystickZ * factor) * tr) * fullr;
  }

  /**
   * Scales the raw power to the settings defined.
   * @param motorPosition left1, left2, right1, right2
   * @return Scaled power val to be used by motors
   */
  public static Double scale(String motorPosition, Double joystickY, Double joystickZ){
    int factor = 1;
    if (motorPosition == "left2" || motorPosition == "right2"){factor = -1;}
    
    return (joystickY * fr + (joystickZ * factor) * tr) * fullr;
  }
}

class drive {
  int finalpr, pr, subpr;
  boolean interrupt_, allow;

  /**
   * Creates instance of a drive controller
   * @param priority Main priority of the instance
   * @param subPriority Priority of instance if equal to other priorities
   * @param interrupt If the instance will end lower prioritized tasks
   */
  public drive(int priority, int subPriority, Boolean interrupt){
    subpr = subPriority;
    pr = priority;
    finalpr = priority;
    interrupt_ = interrupt;
  }

  /**
   * Checks if this instance should be ran, if another object has a higher
   * priority it will not run.
   * @param objects All instances of other arcadeDrives
   */
  public void periodic(drive... objects){
    for (drive i : objects){ // i.interrupt is added to check if i wants the other process to be interrupted
      if (i.pr > pr && i.interrupt_){allow = false;} // Checks with other instances to see if it should be prioritized
      else if (i.pr == pr && i.interrupt_){ // Checks with subpr if theyre equal
        if (i.subpr > pr){allow = false;}
        else{allow = true;}
      }
      else{allow = true;}
    }
  }

  /**
   * Arcade drive refers to a tank drive train robot being controlled
   * by a single joystick, much like an arcade game.
   * @param map speedmapper instances for all 4 motors
   */
  public void arcdrive(Double joystickY, Double  joystickZ,
                      Speedmapper mapLeft1, Speedmapper mapLeft2, Speedmapper mapRight1, Speedmapper mapRight2){
  if (joystickY == 0 && joystickZ == 0){ // If not running, will set priority to 0
    ArcadeDriveTrain.drive(0.0, 0.0, 0.0, 0.0);
    setPriority(0);
  }else{setPriority(finalpr);} // resets pr back
  
  if (allow) {

      ArcadeDriveTrain.drive(
        mapLeft1.map(ArcadeDriveTrain.leftE1.getVelocity(), joystickY, joystickZ), 
        mapLeft2.map(ArcadeDriveTrain.leftE2.getVelocity(), joystickY, -joystickZ),
        mapRight1.map(ArcadeDriveTrain.rightE1.getVelocity(), joystickY, joystickZ),
        mapRight2.map(ArcadeDriveTrain.rightE2.getVelocity(), joystickY, -joystickZ));
      
    }
  }

  /**
   * Arcade drive refers to a tank drive train robot being controlled
   * by a single joystick, much like an arcade game.
   */
  public void arcdrive(Double joystickY, Double  joystickZ){ // Override method
  if (joystickY == 0 && joystickZ == 0){ // If not running, will set priority to 0
    ArcadeDriveTrain.drive(0.0, 0.0, 0.0, 0.0);
    setPriority(0);
  }else{setPriority(finalpr);} // resets pr back
  
  if (allow) {

      ArcadeDriveTrain.drive(
        ArcadeDriveTrain.scale("left1", joystickY, joystickZ),
        ArcadeDriveTrain.scale("left2", joystickY, joystickZ),
        ArcadeDriveTrain.scale("right1", joystickY, joystickZ),
        ArcadeDriveTrain.scale("right2", joystickY, joystickZ)
      );
      
    }
  }

  /**
   * Manual controls for all motors
   * @param left1 motor power val
   * @param left2 motor power val 
   * @param right1 motor power val
   * @param right2 motor power val
   */ 
  public void mandrive(Double left1, Double left2, Double right1, Double right2){
    if (left1 == 0 && left2 == 0 && right1 == 0 && right2 == 0){ // If not running, will set priority to 0
      ArcadeDriveTrain.drive(0.0, 0.0, 0.0, 0.0);
      setPriority(0);
    }else{setPriority(finalpr);} // resets pr back
    
    if (allow) {
      ArcadeDriveTrain.drive(left1, left2, right1, right2);
    }
  }
  /**
   * Sets the priority of this instance
   * @param priority int of prio. Higher the more favored
   */
  public void setPriority(int priority){
    pr = priority;
    finalpr = priority;
  }
}

class Speedmapper {
  public int pte = 5676, tolerance_ = 0;
  public double count = 0, delta_ = 0.1;

  /**
   * A fix for speedmapping that uses a controlled base to map speeds from.
   * Using a class so that each instance can have its own data
   * @param PtoEScale scale of encoder
   * @param delta change to try
   * @param tolerance allowed error
   */
  public Speedmapper(int PtoEScale, Double delta, int tolerance){
      pte = PtoEScale;
      count = 0; // Count starts always at 0
      delta_ = delta;
      tolerance_ = tolerance;
  }

  /**
   * Uses encoders to scale values of motors to all match, usefull
   * when motor values have to be exact
   * @apiNote Requires encoders on each drivetrain motors
   * @param encoderval the encoder value of the val motor
   * @param JoystickF forward joystick used to create a target to match the value to
   * @param JoystickT twist joystick used to create a target to match the value to
   * @param Frate the forward rate, generally equal to forwardRate.
   * @param Trate the twist rate, generally equal to twistRate.
   * @param tolerance the allowed difference between the val and joystick.
   */
  public double map(Double encoderval, Double JoystickF, Double JoystickT){

    Double Frate = ArcadeDriveTrain.fr;
    Double Trate = ArcadeDriveTrain.tr;
    Double FullRate = ArcadeDriveTrain.fullr;

    Double target = ((JoystickF * Frate + JoystickT * Trate) * FullRate) * pte;

    if (encoderval < target - tolerance_){
        count = count + delta_;
    }
    else if (encoderval > target + tolerance_){
        count = count - delta_;
    }

    return (JoystickF * Frate + JoystickT * Trate) + count;
  }
}
