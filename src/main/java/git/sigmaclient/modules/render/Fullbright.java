package git.sigmaclient.modules.render;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;

public class Fullbright extends Module {

    private float originalGamma;

    public Fullbright() {
        super("Fullbright", Category.RENDER);
    }

    @Override
    public void assign()
    {
        SigmaClient.fullbright = this;
    }

    @Override
    public void onEnable() {
        originalGamma = SigmaClient.mc.gameSettings.gammaSetting;
        SigmaClient.mc.gameSettings.gammaSetting = 100;
    }

    @Override
    public void onDisable() {
        SigmaClient.mc.gameSettings.gammaSetting = originalGamma > 10 ? 1 : originalGamma;
        if(SigmaClient.clientSettings.debug.isEnabled()) {
            SigmaClient.sendMessageWithPrefix("" + SigmaClient.mc.gameSettings.gammaSetting);
        }
    }
}