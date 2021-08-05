// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ManualArcadeDrive;
import frc.robot.subsystems.ArcadeDriveTrain;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  Joystick driver = new Joystick(0);
  Joystick operator = new Joystick(1);

  // The robot's subsystems and commands are defined here...
  private final ArcadeDriveTrain driveTrain = new ArcadeDriveTrain(0.4, 1.0, 1.0);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // Define default commands
    driveTrain.setDefaultCommand(new ManualArcadeDrive(driveTrain,
      () -> driver.getY(),
      () -> driver.getZ(),
      () -> driver.getTrigger()
    ));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    JoystickButton d1 = new JoystickButton(driver, 1);
    JoystickButton d2 = new JoystickButton(driver, 2);
    JoystickButton d3 = new JoystickButton(driver, 3);
    JoystickButton d4 = new JoystickButton(driver, 4);
    JoystickButton d5 = new JoystickButton(driver, 5);
    JoystickButton d6 = new JoystickButton(driver, 6);
    JoystickButton d7 = new JoystickButton(driver, 7);
    JoystickButton d8 = new JoystickButton(driver, 8);
    JoystickButton d9 = new JoystickButton(driver, 9);
    JoystickButton d10= new JoystickButton(driver, 10);
    JoystickButton d11= new JoystickButton(driver, 11);
    JoystickButton d12= new JoystickButton(driver, 12);

    JoystickButton o1 = new JoystickButton(operator, 1);
    JoystickButton o2 = new JoystickButton(operator, 2);
    JoystickButton o3 = new JoystickButton(operator, 3);
    JoystickButton o4 = new JoystickButton(operator, 4);
    JoystickButton o5 = new JoystickButton(operator, 5);
    JoystickButton o6 = new JoystickButton(operator, 6);
    JoystickButton o7 = new JoystickButton(operator, 7);
    JoystickButton o8 = new JoystickButton(operator, 8);
    JoystickButton o9 = new JoystickButton(operator, 9);
    JoystickButton o10= new JoystickButton(operator, 10);
    JoystickButton o11= new JoystickButton(operator, 11);
    JoystickButton o12= new JoystickButton(operator, 12);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null; // TODO: auto
  }
}
