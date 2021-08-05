// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Libraries.DeadZoneTuner;
import frc.robot.subsystems.ArcadeDriveTrain;

public class ManualArcadeDrive extends CommandBase {
  ArcadeDriveTrain drive;
  DoubleSupplier jsY, jsZ;
  BooleanSupplier trigger;
  DeadZoneTuner tuner = new DeadZoneTuner();
  /** Creates a new ManualArcadeDrive. */
  public ManualArcadeDrive(
    ArcadeDriveTrain arcadeDriveTrain, 
    DoubleSupplier JoystickY, DoubleSupplier JoystickZ, 
    BooleanSupplier JoystickTrigger
  ){
    drive = arcadeDriveTrain;

    jsY = JoystickY;
    jsZ = JoystickZ;

    trigger = JoystickTrigger;

    addRequirements(drive);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    // Uses standerd arcade drive with no motor correction
    double jsYtuned = tuner.adjustForDeadzone(jsY.getAsDouble(), 0.2, false);
    double jsZtuned = tuner.adjustForDeadzone(jsZ.getAsDouble(), 0.3, false);
    drive.arcadeDrive(jsZtuned, jsYtuned, true, false);
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
