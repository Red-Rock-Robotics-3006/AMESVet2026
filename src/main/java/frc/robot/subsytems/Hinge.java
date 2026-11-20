package frc.robot.subsytems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.redrocklib.logging.SmartDashboardNumber;
import frc.robot.redrocklib.wrappers.RedRockTalon;

public class Hinge extends SubsystemBase {

    private static Hinge instance = null;
    
    private final RedRockTalon hingeMain = new RedRockTalon(0, "Hinge/Hinge Motor Main");//TODO
    // private final RedRockTalon hingeFollow = new RedRockTalon(0, "Hinge/Hinge Motor Follow");

    private SmartDashboardNumber currentThreshold = new SmartDashboardNumber("End Effector/Current Threshold", 50);
    private SmartDashboardNumber normalSpeed = new SmartDashboardNumber("Hinge/Reset/Normal Speed", -0.1);

    @SuppressWarnings(value = { "unused" })
    private SmartDashboardNumber stowAngle = new SmartDashboardNumber("Hinge/Angles/Stow Angle", 0);

    private SmartDashboardNumber intakeShellAngle = new SmartDashboardNumber("Hinge/Angles/Intake Angles/ Shell Angle", 10);
    private SmartDashboardNumber intakeCellAngle = new SmartDashboardNumber("Hinge/Angles/Intake Angles/ Cell Angle", 20);

    private SmartDashboardNumber outakeNestedAngle = new SmartDashboardNumber("Hinge/Angles/Outake Angles/ Nested Angle", 30);
    private SmartDashboardNumber outakeDockAngle = new SmartDashboardNumber("Hinge/Angles/Outake Angles/ Dock Angle", 40);
    private SmartDashboardNumber outakeL1Angle = new SmartDashboardNumber("Hinge/Angles/Outake Angles/ L1 Angle", 50);
    private SmartDashboardNumber outakeL2Angle = new SmartDashboardNumber("Hinge/Angles/Outake Angles/ L2 Angle", 60);
    private SmartDashboardNumber outakeL3Angle = new SmartDashboardNumber("Hinge/Angles/Outake Angles/ L3 Angle", 70);
    private SmartDashboardNumber outakeSecondaryL3Angle = new SmartDashboardNumber("Hinge/Angles/Outake Angles/ Special L3 Angle", 80);
    
    private Hinge() {
        super();

        this.hingeMain.withMotorOutputConfigs(
        new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive)
                .withPeakForwardDutyCycle(1d)
                .withPeakReverseDutyCycle(-1d)
                .withNeutralMode(NeutralModeValue.Coast))
        .withSlot0Configs(
                new Slot0Configs()
                        .withKA(0)
                        .withKS(0)
                        .withKV(0)
                        .withKP(0)
                        .withKI(0)
                        .withKD(0))
        .withMotionMagicConfigs(
                new MotionMagicConfigs()
                        .withMotionMagicAcceleration(850)
                        .withMotionMagicCruiseVelocity(150)
                        .withMotionMagicJerk(10000000))
        .withSpikeThreshold(17)
        .withCurrentLimitConfigs(
                new CurrentLimitsConfigs()
                        .withSupplyCurrentLimit(45)
                        .withSupplyCurrentLimitEnable(true)
                        .withStatorCurrentLimit(60)
                        .withStatorCurrentLimitEnable(true))
        .withTuningEnabled(false)
        .withFollowerMotor(new TalonFX(0), MotorAlignmentValue.Opposed); //TODO
    }

    private void setPosition(double position) {
        hingeMain.setMotionMagicPosition(position);
    }

    private void setNormalSpeed() {
        hingeMain.setMotionMagicVelocity(normalSpeed.getNumber());
    }

    private void stopMotors() {
        hingeMain.setMotionMagicVelocity(0);
    }

    private void resetKnownPos() {
        hingeMain.resetMotor();
    }

    //#region  ----Commands as FAT as Your MOM----
    public Command intakeCellAngleCommand() {
        return Commands.runOnce(() -> setPosition(intakeCellAngle.getNumber()), this);
    }
    
    public Command intakeShellAngleCommand() {
        return Commands.runOnce(() -> setPosition(intakeShellAngle.getNumber()), this);
    }

    public Command outakeCellAngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeNestedAngle.getNumber()), this);
    }

    public Command outakeShellAngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeDockAngle.getNumber()), this);
    }

    public Command outakeNestedAngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeNestedAngle.getNumber()), this);
    }

    public Command outakeL1AngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeL1Angle.getNumber()), this);
    }

    public Command outakeL2AngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeL2Angle.getNumber()), this);
    }

    public Command outakeL3AngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeL3Angle.getNumber()), this);
    }

    public Command stowAngleCommand() {
        return new FunctionalCommand(
                () -> setNormalSpeed(), 
                () -> {}, 
                (interrupted) -> {
                    stopMotors();
                    resetKnownPos();
                }, 
                () -> {
                        return hingeMain.motor.getSupplyCurrent().getValueAsDouble() > currentThreshold.getNumber();
                },
                this
        );
    }//TODO make stowing reset the angle and rely on current limitations and hardstop

    public Command outakeSecondaryL3AngleCommand() {
        return Commands.runOnce(() -> setPosition(outakeSecondaryL3Angle.getNumber()), this);
    }
    //#endregion

    public static Hinge getInstance() {
        if (instance == null) {
            instance = new Hinge();
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    public void periodic() {
        hingeMain.update();
    }
}