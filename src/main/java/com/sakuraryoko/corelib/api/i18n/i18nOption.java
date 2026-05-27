/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
 *
 * CoreLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoreLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CoreLib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sakuraryoko.corelib.api.i18n;

// TODO; Add more languages as needed
public enum i18nOption
{
	AF_ZA   ("af_za", "Afrikaans"),
	AR_SA   ("ar_sa", "العربية (العالم العربي)"),
	AST_ES  ("ast_es","Asturianu"),
	AZ_AZ   ("az_az", "Azərbaycanca"),
	BA_RU   ("ba_ru", "Башҡортса"),
	BAR     ("bar",   "Bairisch"),
	BE_BY   ("be_by", "Беларуская"),
	BG_BG   ("bg_bg", "Български"),
	BR_FR   ("br_fr", "Brezhoneg"),
	BRB     ("brb",   "Brabants"),
	BS_BA   ("bs_ba", "Bosanski"),
	CA_ES   ("ca_es", "Català (Catalunya)"),
	CS_CZ   ("cs_cz", "Čeština (Česko)"),
	CY_GB   ("cy_gb", "Cymraeg"),
	DA_DK   ("da_dk", "Dansk"),
	DE_AT   ("de_at", "Deutsch (Österreich)"),
	DE_CH   ("de_ch", "Deutsch (Schweiz)"),
	DE_DE   ("de_de", "Deutsch (Deutschland)"),
	EL_GR   ("el_gr", "Ελληνικά (Ελλάδα)"),
	EN_AU   ("en_au", "English (Australia)"),
	EN_GB   ("en_gb", "English (UK)"),
	EN_NZ   ("en_nz", "English (New Zealand)"),
	EN_PT   ("en_pt", "Pirate Speak"),
	EN_UD   ("en_ud", "sɥslƃuǝ (ʞ∩)"),
	EN_US   ("en_us", "English (US)"),
	EO_UY   ("eo_uy", "Esperanto"),
	ES_AR   ("es_ar", "Español (Argentina)"),
	ES_CL   ("es_cl", "Español (Chile)"),
	ES_EC   ("es_ec", "Español (Ecuador)"),
	ES_ES   ("es_es", "Español (España)"),
	ES_MX   ("es_mx", "Español (México)"),
	ES_UY   ("es_uy", "Español (Uruguay)"),
	ES_VE   ("es_ve", "Español (Venezuela)"),
	ET_EE   ("et_ee", "Eesti"),
	EU_ES   ("eu_es", "Euskara"),
	FA_IR   ("fa_ir", "فارسی"),
	FI_FI   ("fi_fi", "Suomi (Suomi)"),
	FIL_PH  ("fil_ph","Filipino (Pilipinas)"),
	FO_FO   ("fo_fo", "Føroyskt"),
	FR_CA   ("fr_ca", "Français (Canada)"),
	FR_FR   ("fr_fr", "Français (France)"),
	FRA_DE  ("fra_de","Fränkisch"),
	FY_NL   ("fy_nl", "Frysk"),
	GA_IE   ("ga_ie", "Gaeilge (Éire)"),
	GD_GB   ("gd_gb", "Gàidhlig"),
	GL_ES   ("gl_es", "Galego"),
	HAW_US  ("haw_us","ʻŌlelo Hawaiʻi"),
	HE_IL   ("he_il", "עברית"),
	HI_IN   ("hi_in", "हिंदी (भारत)"),
	HR_HR   ("hr_hr", "Hrvatski (Hrvatska)"),
	HU_HU   ("hu_hu", "Magyar (Magyarország)"),
	HY_AM   ("hy_am", "Հայերեն"),
	ID_ID   ("id_id", "Bahasa Indonesia"),
	IG_NG   ("ig_ng", "Igbo (Naigeria)"),
	IS_IS   ("is_is", "Íslenska (Ísland)"),
	IT_IT   ("it_it", "Italiano (Italia)"),
	JA_JP   ("ja_jp", "日本語 (日本)"),
	JBO_EN  ("jbo_en","Lojban"),
	KA_GE   ("ka_ge", "ქართული"),
	KK_KZ   ("kk_kz", "Қазақша"),
	KN_IN   ("kn_in", "ಕನ್ನಡ"),
	KO_KR   ("ko_kr", "한국어 (대한민국)"),
	KSH     ("ksh",   "Kölsch"),
	KW_GB   ("kw_gb", "Kernewek"),
	LA_LA   ("la_la", "Latina (Latium)"),
	LB_LU   ("lb_lu", "Lëtzebuergesch"),
	LI_LI   ("li_li", "Limburgs (Limburg)"),
	LMO     ("lmo",   "Lumbaart"),
	LO_LA   ("lo_la", "ພາສາລາວ"),
	LT_LT   ("lt_lt", "Lietuvių"),
	LV_LV   ("lv_lv", "Latviešu"),
	LZH     ("lzh",   "文言 (華夏)"),
	MK_MK   ("mk_mk", "Македонски (Северна Македонија)"),
	MN_MN   ("mn_mn", "Монгол"),
	MS_MY   ("ms_my", "Bahasa Melayu"),
	MT_MT   ("mt_mt", "Malti"),
	NAH     ("nah",   "Nāhuatl"),
	NDS_DE  ("nds_de","Plattdüütsch"),
	NL_BE   ("nl_be", "Vlaams (België)"),
	NL_NL   ("nl_nl", "Nederlands (Nederland)"),
	NN_NO   ("nn_no", "Norsk nynorsk"),
	NO_NO   ("no_no", "Norsk bokmål"),
	OC_FR   ("oc_fr", "Occitan"),
	PL_PL   ("pl_pl", "Polski (Polska)"),
	PT_BR   ("pt_br", "Português (Brasil)"),
	PT_PT   ("pt_pt", "Português (Portugal)"),
	QYA_AA  ("qya_aa","Quenya"),
	RO_RO   ("ro_ro", "Română (România)"),
	RU_RU   ("ru_ru", "Русский (Россия)"),
	SE_NO   ("se_no", "Davvisámegiella (Sápmi)"),
	SK_SK   ("sk_sk", "Slovenčina (Slovensko)"),
	SL_SI   ("sl_si", "Slovenščina (Slovenija)"),
	SO_SO   ("so_so", "Af-Soomaali (Soomaaliya)"),
	SQ_AL   ("sq_al", "Shqip (Shqiperia)"),
	SR_SP   ("sr_sp", "Српски"),
	SV_SE   ("sv_se", "Svenska (Sverige)"),
	TA_IN   ("ta_in", "தமிழ் (இந்தியா)"),
	TH_TH   ("th_th", "ไทย (ประเทศไทย)"),
	TL_PH   ("tl_ph", "Tagalog (Pilipinas)"),
	TLH_AA  ("tlh_aa","tlhIngan Hol"),
	TOK     ("tok",   "Toki Pona"),
	TR_TR   ("tr_tr", "Türkçe (Türkiye)"),
	TT_RU   ("tt_ru", "Татарча"),
	UK_UA   ("uk_ua", "Українська (Україна)"),
	VAL_ES  ("val_es","Català (Valencià)"),
	VI_VN   ("vi_vn", "Tiếng Việt (Việt Nam)"),
	YI_DE   ("yi_de", "ייִדיש"),
	YO_NG   ("yo_ng", "Yorùbá (Nàìjíríà)"),
	ZH_CN   ("zh_cn", "简体中文 (中国大陆)"),
	ZH_HK   ("zh_hk", "繁體中文 (香港特別行政區)"),
	ZH_TW   ("zh_tw", "繁體中文 (台灣)"),
	ZLM_ARAB("zlm_arab","بهاس ملايو (مليسيا)"),
	UNKNOWN ("unknown","UNK: 'xxx' not found"),
	;

	private final String key;
	private final String translatedName;
	private String description;

	i18nOption(String key, String translatedName)
	{
		this.key = key;
		this.translatedName = translatedName;
		this.description = "";
	}

	public String getKey()
	{
		return key;
	}

	public String getTranslatedName()
	{
		if (!this.description.isEmpty())
		{
			return this.description;
		}

		return translatedName;
	}

	private i18nOption setDescription(String description)
	{
		this.description = description;
		return this;
	}

	public static i18nOption fromString(String key)
	{
		// Remove file extension, if present.
		if (key.contains(".json"))
		{
			key = key.replace(".json", "");
		}

		for (i18nOption e : values())
		{
			if (e.key.equalsIgnoreCase(key))
			{
				return e;
			}
		}

		// Not Mapped
		return UNKNOWN.setDescription(String.format("Custom (%s)", key));
	}
}
