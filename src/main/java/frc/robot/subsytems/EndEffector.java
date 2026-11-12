package frc.robot.subsytems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.CoastOut;
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

public class EndEffector extends SubsystemBase {
    private static EndEffector instance = null;

    private RedRockTalon motorTop = new RedRockTalon(0); //TODO
    // private RedRockTalon motorBottom = new RedRockTalon(0);

    private SmartDashboardNumber intakeNormalSpeed = new SmartDashboardNumber("End Effector/Normal Intake Speed", -0.1);
    private SmartDashboardNumber outakeL1Speed = new SmartDashboardNumber("End Effector/Outake L1 Speed", 5);
    private SmartDashboardNumber outakeL2Speed = new SmartDashboardNumber("End Effector/Outake L2 Speed", 5);
    private SmartDashboardNumber outakeL3Speed = new SmartDashboardNumber("End Effector/Outake L3 Speed", 5);

    private SmartDashboardNumber currentThreshold = new SmartDashboardNumber("End Effector/Current Threshold", 50);

    EndEffector() {
        super();

        this.motorTop.withMotorOutputConfigs(
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

    private void setIntakeNormal() {
        motorTop.setMotionMagicVelocity(intakeNormalSpeed.getNumber());
    }

    private void stopMotors() {
        motorTop.motor.setControl(new CoastOut());
    }

    private void setL1Outake() {
        motorTop.setMotionMagicVelocity(outakeL1Speed.getNumber());
    }

    private void setL2Outake() {
        motorTop.setMotionMagicVelocity(outakeL2Speed.getNumber());
    }

    private void setL3Outake() {
        motorTop.setMotionMagicVelocity(outakeL3Speed.getNumber());
    }

    //#region Commands
    public Command intakeCommand(){
        return new FunctionalCommand(
                () -> setIntakeNormal(), 
                () -> {}, 
                (interrupted) -> stopMotors(), 
                () -> {
                        return motorTop.motor.getSupplyCurrent().getValueAsDouble() > currentThreshold.getNumber();
                },
                this
        );
    }

    public Command outakeL1Command() {
        return Commands.runOnce(() -> setL1Outake(), this);
    }

    public Command outakeL2Command() {
        return Commands.runOnce(() -> setL2Outake(), this);
    }

    public Command outakeL3Command() {
        return Commands.runOnce(() -> setL3Outake(), this);
    }

    public Command stopEndEffectorCommand() {
        return Commands.runOnce(() -> stopMotors(), this);
    }
    //#endregion

    public static EndEffector getInstance() {
        if (instance == null) {
            instance = new EndEffector();
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    public void periodic() {
        motorTop.update();
    }
}