package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.sensor.IRSeekerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.BrickKeyPressMode;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.CompassSensorMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.hardware.ev3.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.RobotPythonVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public class PythonVisitor extends RobotPythonVisitor implements AstSensorsVisitor<Void>, AstActorCommunicationVisitor<Void>, AstActorDisplayVisitor<Void>,
    AstActorMotorVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void> {
    protected final EV3Configuration brickConfiguration;

    protected final Map<String, String> predefinedImage = new HashMap<>();

    protected final Set<UsedSensor> usedSensors;
    protected final Set<UsedActor> usedActors;
    protected final Set<String> usedImages;

    protected ILanguage language;
    private final boolean isSayTextUsed;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private PythonVisitor(EV3Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation, ILanguage language) {
        super(programPhrases, indentation);

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);

        this.brickConfiguration = brickConfiguration;

        this.usedActors = checkVisitor.getUsedActors();
        this.usedSensors = checkVisitor.getUsedSensors();
        this.usedImages = checkVisitor.getUsedImages();
        this.isSayTextUsed = checkVisitor.isSayTextUsed();

        this.usedGlobalVarInFunctions = checkVisitor.getMarkedVariablesAsGlobal();
        this.isProgramEmpty = checkVisitor.isProgramEmpty();
        this.loopsLabels = checkVisitor.getloopsLabelContainer();

        this.language = language;

        initPredefinedImages();
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public static String generate(
        EV3Configuration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> programPhrases,
        boolean withWrapping,
        ILanguage language) {
        Assert.notNull(brickConfiguration);

        PythonVisitor astVisitor = new PythonVisitor(brickConfiguration, programPhrases, 0, language);
        astVisitor.generateCode(withWrapping);

        return astVisitor.sb.toString();
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.waitFor(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.waitFor(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("hal.clearDisplay()");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                this.sb.append(")");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de";
            case ENGLISH:
                return "en";
            case FRENCH:
                return "fr";
            case SPANISH:
                return "es";
            case ITALIAN:
                return "it";
            case DUTCH:
                return "nl";
            case FINNISH:
                return "fi";
            case POLISH:
                return "pl";
            case RUSSIAN:
                return "ru";
            case TURKISH:
                return "tu";
            case CZECH:
                return "cs";
            case PORTUGUESE:
                return "pt-pt";
            case DANISH:
                return "da";
            default:
                return "en";
        }
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        this.sb.append("hal.setLanguage(\"");
        this.sb.append(this.getLanguageString(setLanguageAction.getLanguage()));
        this.sb.append("\")");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("hal.sayText(");
        if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            sayTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            sayTextAction.getMsg().visit(this);
        }
        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( !(sayTextAction.getSpeed().getKind().equals(emptyBlock) && sayTextAction.getPitch().getKind().equals(emptyBlock)) ) {
            this.sb.append(",");
            sayTextAction.getSpeed().visit(this);
            this.sb.append(",");
            sayTextAction.getPitch().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("hal.ledOn(" + getEnumCode(lightAction.getColor()) + ", " + getEnumCode(lightAction.getBlinkMode()) + ")");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                this.sb.append("hal.ledOff()");
                break;
            case RESET:
                this.sb.append("hal.resetLED()");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.playFile(" + playFileAction.getFileName() + ")");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.sb.append("hal.drawPicture(predefinedImages['").append(showPictureAction.getPicture()).append("'], ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("hal.drawText(");
        if ( !showTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("hal.playTone(float(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append("), float(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append("))");
        return null;
    }

    private boolean isActorOnPort(IActorPort port) {
        boolean isActorOnPort = false;
        for ( UsedActor actor : this.usedActors ) {
            isActorOnPort = isActorOnPort ? isActorOnPort : actor.getPort().equals(port);
        }
        return isActorOnPort;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        if ( isActorOnPort(motorOnAction.getPort()) ) {
            String methodName;
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorOnAction.getPort());
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( duration ) {
                methodName = isRegulated ? "hal.rotateRegulatedMotor('" : "hal.rotateUnregulatedMotor('";
            } else {
                methodName = isRegulated ? "hal.turnOnRegulatedMotor('" : "hal.turnOnUnregulatedMotor('";
            }
            this.sb.append(methodName + motorOnAction.getPort().toString() + "', ");
            motorOnAction.getParam().getSpeed().visit(this);
            if ( duration ) {
                this.sb.append(", " + getEnumCode(motorOnAction.getDurationMode()));
                this.sb.append(", ");
                motorOnAction.getDurationValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        if ( isActorOnPort(motorSetPowerAction.getPort()) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
            String methodName = isRegulated ? "hal.setRegulatedMotorSpeed('" : "hal.setUnregulatedMotorSpeed('";
            this.sb.append(methodName + motorSetPowerAction.getPort().toString() + "', ");
            motorSetPowerAction.getPower().visit(this);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        if ( isActorOnPort(motorGetPowerAction.getPort()) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorGetPowerAction.getPort());
            String methodName = isRegulated ? "hal.getRegulatedMotorSpeed('" : "hal.getUnregulatedMotorSpeed('";
            this.sb.append(methodName + motorGetPowerAction.getPort().toString() + "')");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( isActorOnPort(motorStopAction.getPort()) ) {
            this.sb.append("hal.stopMotor('").append(motorStopAction.getPort().toString()).append("', ").append(getEnumCode(motorStopAction.getMode())).append(
                ')');
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        if ( isActorOnPort(this.brickConfiguration.getLeftMotorPort()) && isActorOnPort(this.brickConfiguration.getRightMotorPort()) ) {
            boolean isDuration = driveAction.getParam().getDuration() != null;
            String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
            this.sb.append(methodName);
            this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
            this.sb.append("'" + this.brickConfiguration.getRightMotorPort().toString() + "', False, ");
            this.sb.append(getEnumCode(driveAction.getDirection()) + ", ");
            driveAction.getParam().getSpeed().visit(this);
            if ( isDuration ) {
                this.sb.append(", ");
                driveAction.getParam().getDuration().getValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        if ( isActorOnPort(this.brickConfiguration.getLeftMotorPort()) && isActorOnPort(this.brickConfiguration.getRightMotorPort()) ) {
            boolean isDuration = turnAction.getParam().getDuration() != null;
            boolean isRegulated = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated();
            String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : isRegulated ? "Regulated" : "Unregulated") + "(";
            this.sb.append(methodName);
            this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
            this.sb.append("'" + this.brickConfiguration.getRightMotorPort().toString() + "', False, ");
            this.sb.append(getEnumCode(turnAction.getDirection()) + ", ");
            turnAction.getParam().getSpeed().visit(this);
            if ( isDuration ) {
                this.sb.append(", ");
                turnAction.getParam().getDuration().getValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        if ( isActorOnPort(this.brickConfiguration.getLeftMotorPort()) && isActorOnPort(this.brickConfiguration.getRightMotorPort()) ) {
            this.sb.append("hal.stopMotors(");
            this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
            this.sb.append("'" + this.brickConfiguration.getRightMotorPort().toString() + "')");
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        if ( isActorOnPort(this.brickConfiguration.getLeftMotorPort()) && isActorOnPort(this.brickConfiguration.getRightMotorPort()) ) {
            MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();

            this.sb.append("hal.driveInCurve(");
            this.sb.append(getEnumCode(curveAction.getDirection()) + ", ");
            this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
            curveAction.getParamLeft().getSpeed().visit(this);
            this.sb.append(", '" + this.brickConfiguration.getRightMotorPort().toString() + "', ");
            curveAction.getParamRight().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", ");
                duration.getValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        switch ( (BrickKeyPressMode) brickSensor.getMode() ) {
            case PRESSED:
                this.sb.append("hal.isKeyPressed(" + getEnumCode(brickSensor.getPort()) + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isKeyPressedAndReleased(" + getEnumCode(brickSensor.getPort()) + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String colorSensorPort = colorSensor.getPort().getPortNumber();
        switch ( (ColorSensorMode) colorSensor.getMode() ) {
            case AMBIENTLIGHT:
                this.sb.append("hal.getColorSensorAmbient('" + colorSensorPort + "')");
                break;
            case COLOUR:
                this.sb.append("hal.getColorSensorColour('" + colorSensorPort + "')");
                break;
            case LIGHT:
                this.sb.append("hal.getColorSensorRed('" + colorSensorPort + "')");
                break;
            case RGB:
                this.sb.append("hal.getColorSensorRgb('" + colorSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalide mode for Color Sensor!");
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderSensorPort = encoderSensor.getPort().toString();
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            this.sb.append("hal.resetMotorTacho('" + encoderSensorPort + "')");
        } else {
            this.sb.append("hal.getMotorTachoValue('" + encoderSensorPort + "', " + getEnumCode(encoderSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String gyroSensorPort = gyroSensor.getPort().getPortNumber();
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            this.sb.append("hal.resetGyroSensor('" + gyroSensorPort + "')");
        } else {
            this.sb.append("hal.getGyroSensorValue('" + gyroSensorPort + "', " + getEnumCode(gyroSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        String compassSensorPort = compassSensor.getPort().getPortNumber();
        switch ( (CompassSensorMode) compassSensor.getMode() ) {
            case CALIBRATE:
                // Calibration is not supported by ev3dev hitechnic sensor for now
                break;
            case ANGLE:
            case COMPASS:
                this.sb.append("hal.getHiTecCompassSensorValue('" + compassSensorPort + "', " + getEnumCode(compassSensor.getMode()) + ")");
                break;
            default:
                throw new DbcException("Invalid Compass Mode!");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String infraredSensorPort = infraredSensor.getPort().getPortNumber();
        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
            case DISTANCE:
                this.sb.append("hal.getInfraredSensorDistance('" + infraredSensorPort + "')");
                break;
            case PRESENCE:
                this.sb.append("hal.getInfraredSensorSeek('" + infraredSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }

        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        String irSeekerSensorPort = irSeekerSensor.getPort().getPortNumber();
        switch ( (IRSeekerSensorMode) irSeekerSensor.getMode() ) {
            case MODULATED:
                this.sb.append("hal.getHiTecIRSeekerSensorValue('" + irSeekerSensorPort + "', 'AC')");
                break;
            case UNMODULATED:
                this.sb.append("hal.getHiTecIRSeekerSensorValue('" + irSeekerSensorPort + "', 'DC')");
                break;
            default:
                throw new DbcException("Invalid IRSeeker Mode!");
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort().getPortNumber();
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("hal.getTimerValue(" + timerNumber + ")");
                break;
            case RESET:
                this.sb.append("hal.resetTimer(" + timerNumber + ")");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.isPressed('" + touchSensor.getPort().getPortNumber() + "')");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String ultrasonicSensorPort = ultrasonicSensor.getPort().getPortNumber();
        if ( ultrasonicSensor.getMode() == UltrasonicSensorMode.DISTANCE ) {
            this.sb.append("hal.getUltraSonicSensorDistance('" + ultrasonicSensorPort + "')");
        } else {
            this.sb.append("hal.getUltraSonicSensorPresence('" + ultrasonicSensorPort + "')");
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        String soundSensorPort = soundSensor.getPort().getPortNumber();
        this.sb.append("hal.getSoundLevel('" + soundSensorPort + "')");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        generateUserDefinedMethods();
        this.sb.append("\n").append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(getEnumCode(where1));
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(getEnumCode(where2));
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                this.sb.append("BlocklyMethods.length( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("BlocklyMethods.isEmpty( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        this.sb.append("BlocklyMethods.listsGetIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getElementOperation()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        this.sb.append("BlocklyMethods.listsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getElementOperation()));
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("BlocklyMethods.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("BlocklyMethods.textJoin(");
        textJoinFunct.getParam().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        this.sb.append("hal.readMessage(");
        bluetoothReadAction.getConnection().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        this.sb.append("hal.establishConnectionTo(");
        if ( !bluetoothConnectAction.getAddress().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            bluetoothConnectAction.getAddress().visit(this);
            this.sb.append(")");
        } else {
            bluetoothConnectAction.getAddress().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        this.sb.append("hal.sendMessage(");
        bluetoothSendAction.getConnection().visit(this);
        this.sb.append(", ");
        if ( !bluetoothSendAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            bluetoothSendAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            bluetoothSendAction.getMsg().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        this.sb.append("hal.waitForConnection()");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python\n\n");
        this.sb.append("from __future__ import absolute_import\n");
        this.sb.append("from roberta.ev3 import Hal\n");
        this.sb.append("from roberta.BlocklyMethods import BlocklyMethods\n");
        this.sb.append("from ev3dev import ev3 as ev3dev\n");
        this.sb.append("import math\n\n");

        this.sb.append("class BreakOutOfALoop(Exception): pass\n");
        this.sb.append("class ContinueLoop(Exception): pass\n\n");

        this.sb.append(generateUsedImages());
        this.sb.append(generateRegenerateConfiguration()).append("\n");
        this.sb.append("hal = Hal(_brickConfiguration)");

        if ( this.isSayTextUsed ) {
            this.sb.append("\nhal.setLanguage(\"");
            this.sb.append(this.getLanguageString(this.language));
            this.sb.append("\")");
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(INDENT).append("try:\n");
        this.sb.append(INDENT).append(INDENT).append("run()\n");
        this.sb.append(INDENT).append("except Exception as e:\n");
        this.sb.append(INDENT).append(INDENT).append("hal.drawText('Fehler im EV3', 0, 0)\n");
        this.sb.append(INDENT).append(INDENT).append("hal.drawText(e.__class__.__name__, 0, 1)\n");
        // FIXME: we can only print about 30 chars
        this.sb.append(INDENT).append(INDENT).append("hal.drawText(str(e), 0, 2)\n");
        this.sb.append(INDENT).append(INDENT).append("hal.drawText('Press any key', 0, 4)\n");
        this.sb.append(INDENT).append(INDENT).append("while not hal.isKeyPressed('any'): hal.waitFor(500)\n");
        this.sb.append(INDENT).append(INDENT).append("raise\n");

        this.sb.append("\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(INDENT).append("main()");
    }

    private String generateUsedImages() {
        if ( this.usedImages.size() != 0 ) {
            StringBuilder sb = new StringBuilder();

            sb.append("predefinedImages = {\n");
            for ( String image : this.usedImages ) {
                sb.append("    '" + image + "': u'" + this.predefinedImage.get(image) + "',\n");
            }
            sb.append("}\n");
            return sb.toString();

        } else {
            return "";
        }
    }

    private String generateRegenerateConfiguration() {
        StringBuilder sb = new StringBuilder();
        sb.append("_brickConfiguration = {\n");
        sb.append("    'wheel-diameter': " + this.brickConfiguration.getWheelDiameterCM() + ",\n");
        sb.append("    'track-width': " + this.brickConfiguration.getTrackWidthCM() + ",\n");
        appendActors(sb);
        appendSensors(sb);
        sb.append("}");
        return sb.toString();
    }

    private boolean isActorUsed(Actor actor, IActorPort port) {
        for ( UsedActor usedActor : this.usedActors ) {
            if ( port == usedActor.getPort() && actor.getName() == usedActor.getType() ) {
                return true;
            }
        }
        return false;
    }

    private void appendActors(StringBuilder sb) {
        sb.append("    'actors': {\n");
        for ( Map.Entry<IActorPort, Actor> entry : this.brickConfiguration.getActors().entrySet() ) {
            Actor actor = entry.getValue();
            IActorPort port = entry.getKey();
            if ( actor != null && isActorUsed(actor, port) ) {
                sb.append("        '").append(port.toString()).append("':");
                sb.append(generateRegenerateActor(actor, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private boolean isSensorUsed(Sensor sensor, ISensorPort port) {
        for ( UsedSensor usedSensor : this.usedSensors ) {
            if ( port == usedSensor.getPort() && sensor.getType() == usedSensor.getType() ) {
                return true;
            }
        }
        return false;
    }

    private void appendSensors(StringBuilder sb) {
        sb.append("    'sensors': {\n");
        for ( Map.Entry<ISensorPort, Sensor> entry : this.brickConfiguration.getSensors().entrySet() ) {
            Sensor sensor = entry.getValue();
            ISensorPort port = entry.getKey();
            if ( sensor != null && isSensorUsed(sensor, port) ) {
                sb.append("        '").append(port.getPortNumber()).append("':");
                sb.append(generateRegenerateSensor(sensor, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private String generateRegenerateActor(Actor actor, IActorPort port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        switch ( actor.getName() ) {
            case MEDIUM:
                name = "MediumMotor";
                break;
            case LARGE:
                name = "LargeMotor";
                break;
            case OTHER:
                name = "OtherConsumer";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + actor.getName() + "to ev3dev-lang-python");
        }

        sb.append("Hal.make").append(name).append("(ev3dev.OUTPUT_").append(port.toString());
        sb.append(", ").append(actor.isRegulated() ? "'on'" : "'off'");
        sb.append(", ").append(getEnumCode(actor.getRotationDirection()));
        sb.append(", ").append(getEnumCode(actor.getMotorSide()));
        sb.append(")");
        return sb.toString();
    }

    private void initPredefinedImages() {
        this.predefinedImage.put(
            "OLDGLASSES",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u00fc\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0001\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u003f\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u00f0\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u000f\\u00c0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u00e0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u00e0\\u007f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0080\\u00ff\\u0001\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00fc\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u007f\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00e0\\u003f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00ff\\u0001\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f0\\u003f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f0\\u001f\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u001f\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0080\\u007f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00c0\\u007f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u003f\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u0007\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u003f\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fc\\u0007\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00e0\\u001f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0001\\u00fe\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00f0\\u000f\\u00ff\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00f0\\u001f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00f8\\u001f\\u00ff\\u0003\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u003f\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003e\\u007c\\u00ff\\u0007\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u003f\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u0007\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u00f0\\u00ff\\u001f\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00e3\\u00c7\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00fb\\u00df\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "EYESOPEN",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00cc\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00cc\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00cc\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u008c\\u0037\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u000c\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u000c\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0000\\u0000\\u000c\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u007f\\u0000\\u0000\\u0006\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0006\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0006\\u0007\\u0000\\u0000\\u0000\\u0000\\u0080\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u00c0\\u003f\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0001\\u0000\\u0000\\u0000\\u0000\\u0060\\u0003\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0001\\u0000\\u0000\\u0000\\u0000\\u0060\\u0003\\u0000\\u00f8\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0000\\u0000\\u0000\\u0000\\u0000\\u0020\\u0003\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0018\\u0000\\u0000\\u0000\\u0018\\u0030\\u0003\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u00c3\\u003f\\u0000\\u0000\\u0000\\u00fc\\u0030\\u0003\\u0080\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0083\\u001f\\u0000\\u0000\\u0000\\u00ee\\u003b\\u0003\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0080\\u0001\\u000e\\u0000\\u0000\\u0000\\u0086\\u001f\\u0003\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0080\\u0001\\u0007\\u0000\\u0000\\u0000\\u0006\\u001e\\u0003\\u00f0\\u000f\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u00fc\\u0000\\u0000\\u003f\\u0000\\u0000\\u0080\\u0081\\u0003\\u0000\\u0000\\u0000\\u000c\\u0000\\u0003\\u00f8\\u0007\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u003f\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u009f\\u00c1\\u0001\\u0000\\u0000\\u0000\\u000c\\u0000\\u0003\\u00f8\\u0007\\u0000\\u00fc\\u00ff\\u00ff\\u0003\\u0080\\u000f\\u0000\\u0000\\u00f0\\u0081\\u00ff\\u009f\\u00e1\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0003\\u00e0\\u0003\\u0000\\u00ff\\u0001\\u00f8\\u000f\\u00c0\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u009f\\u0071\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0003\\u00c0\\u0003\\u0080\\u001f\\u0000\\u0080\\u001f\\u00e0\\u0001\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u009f\\u003b\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0003\\u00c0\\u0001\\u00e0\\u0007\\u0000\\u0000\\u007e\\u00f0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u001f\\u001f\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0003\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u000c\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0003\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0079\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0003\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u0003\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0003\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0003\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0006\\u0003\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u000c\\u0003\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f6\\r\\u0003\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u001f\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c6\\u000f\\u0003\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u00c0\\u007f\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0006\\u0003\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00e0\\u00c3\\u0000\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000c\\u0000\\u0007\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00f0\\u0081\\u0001\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0007\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00f0\\u0081\\u0001\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u0000\\u003e\\u0080\\u0003\\u0000\\u00f8\\u0081\\u0003\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u0080\\u00ff\\u0080\\u0003\\u0000\\u00f8\\u0081\\u0003\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u00e0\\u00ff\\u0083\\u0003\\u0000\\u00f8\\u00c3\\u0003\\u00c0\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0006\\u0000\\u003f\\u0000\\u0000\\u00e0\\u000f\\u0083\\u0003\\u0000\\u00f8\\u00ff\\u0003\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0006\\u00f0\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u0000\\u00f8\\u00ff\\u0003\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0006\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u0000\\u00f0\\u00ff\\u0001\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u008e\\u0003\\u0000\\u00f0\\u00ff\\u0001\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u000e\\u0007\\u0000\\u00e0\\u00ff\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u000f\\u0007\\u0000\\u00c0\\u007f\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0007\\u0000\\u0000\\u001f\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u00fb\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u000e\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00fb\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u001e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00f9\\u00ff\\u001f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u001c\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u003c\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u003e\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00c3\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00fb\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u00ef\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00f3\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "EYESCLOSED",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0001\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0001\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0001\\u0000\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u00fc\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u003f\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0003\\u0080\\u000f\\u0000\\u0000\\u00f0\\u0081\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u000f\\u00c0\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u001f\\u00e0\\u0001\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u007e\\u00f0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0079\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u003f\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u00fe\\u00c3\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u007f\\u00f8\\u001f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u00fc\\u00c3\\u00ff\\u007f\\u00f8\\u0001\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0080\\u00ff\\u00c7\\u00ff\\u007f\\u000c\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u003e\\u0080\\u0003\\u0000\\u0000\\u00fc\\u00ff\\u00cf\\u00ff\\u007f\\u000c\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0080\\u00ff\\u0080\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00cf\\u00ff\\u007f\\u000c\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u00e0\\u00ff\\u0083\\u0003\\u0000\\u00fe\\u00ff\\u00ff\\u00c7\\u00ff\\u0007\\u0006\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0000\\u00e0\\u000f\\u0083\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00c1\\u003f\\u0000\\u0006\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u00fe\\u00ff\\u00ff\\u0003\\u00c0\\u0001\\u0000\\u001e\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u0000\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0083\\u00ff\\u007f\\u0000\\u0000\\u00c0\\u0001\\u0000\\u003c\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u008e\\u00c3\\u007f\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u000e\\u00c7\\u0003\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u000f\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u000e\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u001e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u001c\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u003c\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u003e\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0007\\u00e0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00c1\\u00ff\\u00c3\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u00fc\\u00c3\\u001f\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001c\\u0000\\u00c3\\u0001\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00fb\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0003\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0003\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0003\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0080\\u0001\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0080\\u0001\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u00c0\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u00c0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u00ef\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u00e0\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00f3\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00c1\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001c\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0087\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e1\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "FLOWERS",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000c\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0080\\u0007\\u000e\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0010\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0070\\u0000\\u0060\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0038\\u0000\\u00c0\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u007f\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0018\\u0000\\u0080\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00e1\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u000c\\u0000\\u0000\\u000f\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00e7\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00ee\\u001f\\u0080\\u00e1\\u000f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ef\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00fe\\u00ff\\u00c0\\u00fc\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00ff\\u00ff\\u00c3\\u001e\\u00f0\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0003\\u0000\\u0080\\u00ff\\u00ff\\u00ef\\u0007\\u00c0\\u0003\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00ff\\u0001\\u0000\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0007\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00e7\\u00ff\\u000f\\u0000\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00fb\\u00ff\\u003f\\u0000\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u000c\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0003\\u0000\\u00f8\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u001c\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0003\\u0000\\u00f8\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0018\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0007\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0018\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0006\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0030\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0006\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u000e\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0038\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u003f\\u0078\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u007f\\u00f0\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0007\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0018\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0018\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u001c\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u000c\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u001f\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00c0\\u0007\\u00fc\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00e0\\u0001\\u00f8\\u00ff\\u00ff\\u00ff\\u0007\\u00c0\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00e0\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u001f\\u00f0\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0070\\u0000\\u00f0\\u00ff\\u00ff\\u003f\\u00ff\\u007f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0078\\u0000\\u00e0\\u00ff\\u00ff\\u001f\\u00fc\\u000f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0038\\u0000\\u00c0\\u00ff\\u00ff\\u000f\\u0018\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u0038\\u0000\\u0080\\u00ff\\u00ff\\u0007\\u0030\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u001c\\u0000\\u0000\\u00ff\\u00ff\\u0003\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u001c\\u0000\\u0000\\u00fc\\u00ff\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u003f\\u001c\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u003f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u000f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0007\\u0038\\u0000\\u0000\\u0068\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0003\\u0038\\u0000\\u0000\\u0068\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0078\\u0000\\u0000\\u006c\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0070\\u0000\\u0000\\u00c4\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u003f\\u0000\\u00e0\\u0000\\u0000\\u00c2\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u000f\\u0000\\u00e0\\u0001\\u0000\\u00c3\\u0001\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u0007\\u00c0\\u0081\\u0001\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0003\\u0000\\u0000\\u0000\\u001f\\u0070\\u0000\\u0003\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00fe\\u003f\\u0000\\u0007\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0000\\u001e\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u001f\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u00fe\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u00ff\\u00ff\\u0000\\u007e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u007f\\u00f0\\u0080\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u001f\\u00c0\\u00e1\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0007\\u0000\\u00f1\\u00e0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0007\\u0000\\u0033\\u0080\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0003\\u0000\\n\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0003\\u0000\\n\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0004\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0004\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u007f\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0003\\u0000\\u0002\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u007f\\u00fc\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0002\\u0000\\u0006\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u003f\\u00fc\\u00ff\\u00ff\\u00ff\\u001f\\u00c0\\u0007\\u0000\\u0007\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u00ff\\u00ff\\u000f\\u00f0\\u0001\\u0000\\u000f\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u000f\\u00f8\\u00ff\\u00ff\\u00ff\\u000f\\u007c\\u0000\\u00c0\\u000f\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0007\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u001e\\u0000\\u00f0\\u003f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u001e\\u0000\\u00fc\\u00ff\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u000f\\u0000\\u00f8\\u00ff\\u0003\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u003f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u000f\\u0000\\u00f8\\u00ff\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0007\\u0000\\u0000\\u00ff\\u00ff\\u007f\\u0080\\u0007\\u0000\\u00f0\\u00ff\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0080\\u0007\\u0000\\u00f0\\u007f\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u0080\\u0007\\u0000\\u00f0\\u007f\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0080\\u0007\\u0000\\u00f0\\u003f\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u00f0\\u003f\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u000f\\u0000\\u0038\\u0038\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0008\\u0020\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0000\\u0001\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00c1\\u0001\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0002\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u009f\\u0000\\u0000\\u000e\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u00fe\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u00f2\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u00c3\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u000f\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "TACHO",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u007f\\u0009\\u0010\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0007\\u0009\\u0010\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\r\\u0010\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0005\\u0010\\u0000\\u00f9\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u0005\\u0000\\u0000\\u00c9\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0002\\u0000\\u0005\\u0000\\u0000\\u0005\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u003f\\u0002\\u0000\\u0005\\u0000\\u0080\\u0004\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0006\\u0000\\u0002\\u0000\\u0080\\u0002\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0004\\u0000\\u0002\\u0000\\u0080\\u0002\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00c1\\u0000\\u0000\\u0002\\u0000\\u0080\\u0001\\u0040\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u00c0\\u0001\\u0000\\u0002\\u0000\\u0040\\u0001\\u0060\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0080\\u0001\\u0000\\u0002\\u0000\\u00c0\\u0000\\u0020\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u0080\\u0003\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0007\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0013\\u0000\\u0007\\u00cc\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0027\\u0000\\n\\u0008\\u00fa\\u00c0\\u0002\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u002c\\u0000\\u000e\\u0008\\u008a\\u0080\\u0082\\u000f\\u0000\\u0040\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0068\\u0000\\u0014\\u00e8\\u008b\\u0080\\u0082\\u0008\\u0000\\u0020\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0050\\u0000\\u001c\\u0028\\u0088\\u0080\\u00a2\\u0008\\u0000\\u00d0\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0060\\u0000\\u002c\\u0028\\u0088\\u0080\\u00be\\u0008\\u0000\\u0038\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u00c0\\u0000\\u0038\\u00e8\\u00fb\\u0080\\u00a0\\u0008\\u0000\\u000c\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u000f\\u00c0\\u0000\\u0058\\u0000\\u0000\\u0080\\u00a0\\u000f\\u0000\\u0003\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0080\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0001\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0003\\u0000\\u0000\\u00b0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0003\\u0000\\u0000\\u0070\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0006\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u00ec\\u0001\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u000c\\u0060\\u0000\\u00a0\\u0002\\u0000\\u0000\\u0000\\u0028\\u00f8\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0018\\u0040\\u00df\\u00c7\\u0003\\u0000\\u0000\\u0000\\u0028\\u0088\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0040\\u0051\\u0044\\u0005\\u0000\\u0000\\u0000\\u00e8\\u008b\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0040\\u0051\\u00c4\\u000e\\u0000\\u0000\\u0000\\u0028\\u008a\\u0000\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0040\\u0051\\u0084\\n\\u0000\\u0000\\u0000\\u0028\\u008a\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0040\\u0051\\u0084\\u0014\\u0000\\u0000\\u0000\\u00e8\\u00fb\\u0000\\u0080\\u0019\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0000\\u0040\\u00df\\u0007\\u0015\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0029\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u000f\\u0000\\u0000\\u0000\\u0000\\u002b\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0052\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0072\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u0000\\u0000\\u00a4\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0033\\u0000\\u0000\\u0000\\u0000\\u00e4\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0043\\u0000\\u0000\\u0000\\u0000\\u004c\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u008f\\u0001\\u0000\\u0000\\u0000\\u0088\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0071\\u00c6\\u0007\\u0000\\u0000\\u0088\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0081\\u004f\\u00f4\\u0001\\u0000\\u0010\\u0005\\u0000\\u0000\\u0000\\u00c0\\u003e\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0040\\u0014\\u0001\\u0000\\u0010\\u0005\\u0000\\u0000\\u0000\\u0080\\u00a2\\u000f\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u0017\\u0001\\u0000\\u0030\\n\\u0000\\u0000\\u0000\\u0080\\u00a2\\u0008\\u0038\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u0040\\u0014\\u0001\\u0000\\u0020\\u001a\\u0000\\u0000\\u0000\\u0080\\u00be\\u0008\\u0007\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0040\\u0014\\u0001\\u0000\\u0020\\u0014\\u0000\\u0000\\u0000\\u0080\\u00a2\\u00e8\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u00c0\\u00f7\\u0001\\u0000\\u0040\\u002c\\u0000\\u0000\\u0000\\u0080\\u00a2\\u0008\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0028\\u0000\\u0000\\u0000\\u0080\\u00be\\u000f\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0050\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0050\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00a0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00a1\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e1\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0072\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0080\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0020\\u00f8\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0000\\u0020\\u0088\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u000f\\u00e0\\u008b\\u0000\\u0000\\u0000\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00f0\\u0020\\u008a\\u0000\\u0000\\u0000\\u0007\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00c0\\u0027\\u008a\\u0000\\u0000\\u0000\\u0007\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u003f\\u00e0\\u00fb\\u0000\\u0000\\u0000\\u000e\\u0080\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0002\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u0000\\n\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0067\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u0000\\n\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0024\\u0000\\u0009\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u0000\\u0082\\u000f\\u0000\\u0000\\u0000\\u0000\\u0094\\n\\u0079\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u008c\\u0095\\u0048\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u00a2\\u0008\\u0000\\u0000\\u0000\\u0000\\u0094\\u0094\\u0048\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u00be\\u0008\\u0000\\u0000\\u0000\\u0000\\u00a4\\u0054\\u0048\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u0000\\u00a0\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u003f\\u0000\\u00a0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u003f\\u00f8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0067\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u007f\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0001\\u00f8\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u007c\\u0000\\u00e0\\u0007\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00be\\u0000\\u0090\\u000f\\u0000\\u0000\\u00a0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u003e\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u009f\\u0000\\u0010\\u001e\\u0000\\u0000\\u00a0\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0080\\u00c1\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u00cf\\u0000\\u0030\\u001e\\u0000\\u0000\\u00be\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u0080\\u0087\\u0004\\u0012\\u003c\\u0000\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u00c3\\u009b\\u003d\\u0078\\u0020\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u0083\\u0091\\u0018\\u00f8\\u0010\\u0000\\u00be\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0080\\u0080\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u00c3\\u0064\\u0032\\u00f8\\u0009\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u00c1\\u00f1\\u0038\\u00f0\\u0007\\u0000\\u0010\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00c0\\u0001\\u00fa\\u0005\\u00f0\\u000f\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0080\\u00be\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0064\\u0002\\u00f0\\u001f\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0060\\u0000\\u00f0\\u003f\\u0000\\n\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u001f\\u0080\\u00be\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0060\\u0000\\u00f0\\u007f\\u0080\\u0005\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u00fe\\u0007\\u00f0\\u00ff\\u0041\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u001c\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0062\\u0004\\u00f8\\u00ff\\u0033\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u007f\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0063\\u000c\\u0088\\u00ff\\u001f\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u003c\\u0080\\u00be\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0083\\u0003\\u001c\\u0008\\u00ff\\u003f\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0087\\u0003\\u001c\\u000c\\u00fe\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u007c\\u0080\\u00ff\\u0000\\u001e\\u0000\\u0000\\u0000\\u00c0\\u008f\\u00ff\\u001f\\u000e\\u00fc\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0003\\u00f8\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0080\\u009f\\u0007\\u001e\\u0006\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0001\\u00f0\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u003f\\u0003\\u000c\\u0003\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00ff\\u007f\\u0000\\u00f0\\u0001\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u00c0\\u0003\\u0080\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u001f\\u0000\\u00e0\\u0003\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u00e0\\u0001\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u000f\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u00fc\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u001f\\u0000\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u001f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
    }

    private static String generateRegenerateSensor(Sensor sensor, ISensorPort port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        // [m for m in dir(ev3dev) if m.find("_sensor") != -1]
        // ['ColorSensor', 'GyroSensor', 'I2cSensor', 'InfraredSensor', 'LightSensor', 'SoundSensor', 'TouchSensor', 'UltrasonicSensor']
        switch ( sensor.getType() ) {
            case COLOR:
                name = "ColorSensor";
                break;
            case GYRO:
                name = "GyroSensor";
                break;
            case INFRARED:
                name = "InfraredSensor";
                break;
            case TOUCH:
                name = "TouchSensor";
                break;
            case ULTRASONIC:
                name = "UltrasonicSensor";
                break;
            case SOUND:
                name = "SoundSensor";
                break;
            case COMPASS:
                name = "CompassSensor";
                break;
            case IRSEEKER:
                name = "IRSeekerSensor";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + sensor.getType() + "to ev3dev-lang-python");
        }
        sb.append("Hal.make").append(name).append("(ev3dev.INPUT_").append(port.getPortNumber()).append(")");
        return sb.toString();
    }
}
