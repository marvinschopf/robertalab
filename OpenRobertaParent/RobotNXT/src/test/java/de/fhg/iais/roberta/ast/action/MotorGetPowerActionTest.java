package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MotorGetPowerActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-78, y=63], MotorGetPower [port=B]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorGetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorGetPower.xml");
        MotorGetPowerAction<Void> mgp = (MotorGetPowerAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorGetPower.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorGetPower1.xml");
    }

}