package frc.robot.subsytems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.redrocklib.logging.SmartDashboardNumber;
import frc.robot.redrocklib.wrappers.RedRockTalon;

public class EndEffector extends SubsystemBase {
    private static EndEffector instance = null;

    private RedRockTalon motorOne = new RedRockTalon(0);
    private RedRockTalon motorTwo = new RedRockTalon(0);

    private SmartDashboardNumber intakeSpeed = new SmartDashboardNumber("End Effector/Intake Speed", 0);
    private SmartDashboardNumber outakeSpeed = new SmartDashboardNumber("End Effector/Outake Speed", 0);

    EndEffector() {
        super();

        this.motorTwo.withMotorOutputConfigs(
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
                .withTuningEnabled(false);

                this.motorOne.withMotorOutputConfigs(
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
                .withFollowerMotor(motorTwo.motor, null);
    }

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
        motorOne.update();
    }
}