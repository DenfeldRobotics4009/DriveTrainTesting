// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArcadeDriveTrain;

public class ManualArcadeDrive extends CommandBase {
  ArcadeDriveTrain drive;
  Double jsY, jsZ;
  Boolean trigger;
  /** Creates a new ManualArcadeDrive. */
  public ManualArcadeDrive(
    ArcadeDriveTrain arcadeDriveTrain, 
    DoubleSupplier JoystickY, DoubleSupplier JoystickZ, 
    BooleanSupplier JoystickTrigger
  ){
    drive = arcadeDriveTrain;

    jsY = JoystickY.getAsDouble();
    jsZ = JoystickZ.getAsDouble();

    trigger = JoystickTrigger.getAsBoolean();

    addRequirements(drive);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    // Uses standerd arcade drive with no motor correction
    if (trigger){drive.arcadeDrive(jsY*2, jsZ*2, false);} // Speed up
    else {drive.arcadeDrive(jsY, jsZ, false);}
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //TODO: End method?
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
