package mods.DCironchain.common;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LangRegister {

	public void registerLang() {
		
		LanguageRegistry.addName(DCsIronChain.ironChain, "Iron Chain");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.ironChain, "ja_JP", "鉄鎖");
		
		LanguageRegistry.addName(DCsIronChain.anchorBolt, "Anchor Bolt");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.anchorBolt, "ja_JP", "アンカーボルト");
		
		LanguageRegistry.addName(DCsIronChain.kyatatu, "Stepladder");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.kyatatu, "ja_JP", "脚立");
		
		LanguageRegistry.addName(DCsIronChain.floodLight, "Flood Light");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.floodLight, "ja_JP", "投光機");
		
		LanguageRegistry.addName(DCsIronChain.DCLightPart, "Invisible Light Block");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.DCLightPart, "ja_JP", "不可視光源");
		
		LanguageRegistry.addName(DCsIronChain.RHopper, "Upward Hopper");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.RHopper, "ja_JP", "逆向きホッパー");
		
		LanguageRegistry.addName(DCsIronChain.RHopperGold, "Golden Upward Hopper");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.RHopperGold, "ja_JP", "金色の逆向きホッパー");
		
		LanguageRegistry.addName(DCsIronChain.RHopperBlack, "Black Upward Hopper");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.RHopperBlack, "ja_JP", "黒の逆向きホッパー");
		
		LanguageRegistry.addName(DCsIronChain.anzenMet, "Worker Helmet");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.anzenMet, "ja_JP", "安全帽");
		
		LanguageRegistry.addName(DCsIronChain.sagyougi, "Worker Wear");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.sagyougi, "ja_JP", "作業着");
		
		LanguageRegistry.addName(DCsIronChain.anzenBoots, "Worker Boots");
		LanguageRegistry.instance().addNameForObject(DCsIronChain.anzenBoots, "ja_JP", "安全靴");
		
	}

}
