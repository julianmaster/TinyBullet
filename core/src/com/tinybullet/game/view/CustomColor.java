package com.tinybullet.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class CustomColor {
	public static final Color WHITE = new Color(0xDBE0E7FF);
	public static final Color GRIS1 = new Color(0xA3ACBEFF);
	public static final Color GRIS2 = new Color(0x67708BFF);
	public static final Color GRIS3 = new Color(0x4E5371FF);
	public static final Color GRIS4 = new Color(0x393A56FF);
	public static final Color GRIS5 = new Color(0x26243AFF);
	public static final Color BLACK = new Color(0x141020FF);

	public static final Color GREEN1 = new Color(0x7BCF5CFF);
	public static final Color GREEN2 = new Color(0x509B4BFF);
	public static final Color GREEN3 = new Color(0x2E6A42FF);
	public static final Color GREEN4 = new Color(0x1A453BFF);

	public static final Color BLUE1 = new Color(0x41F3FCFF);
	public static final Color BLUE2 = new Color(0x13B2F2FF);
	public static final Color BLUE3 = new Color(0x0E82CEFF);
	public static final Color BLUE4 = new Color(0x0F4DA3FF);
	public static final Color BLUE5 = new Color(0x0D2F6DFF);

	public static final Color BROWN1 = new Color(0xF0D2AFFF);
	public static final Color BROWN2 = new Color(0xE5AE78FF);
	public static final Color BROWN3 = new Color(0xC58158FF);
	public static final Color BROWN4 = new Color(0x945542FF);
	public static final Color BROWN5 = new Color(0x623530FF);
	public static final Color BROWN6 = new Color(0x46211FFF);

	public static final Color YELLOW1 = new Color(0xFBDF6BFF);
	public static final Color YELLOW2 = new Color(0xF7AC37FF);

	public static final Color ORANGE1 = new Color(0xE57028FF);
	public static final Color ORANGE2 = new Color(0x97432AFF);

	public static final Color PINK1 = new Color(0xFE979BFF);
	public static final Color PINK2 = new Color(0xED5259FF);

	public static final Color RED1 = new Color(0xC42C36FF);
	public static final Color RED2 = new Color(0x781F2CFF);
	public static final Color RED3 = new Color(0x351428FF);

	public static final Color PURPLE1 = new Color(0xE38DD6FF);
	public static final Color PURPLE2 = new Color(0xB45EB3FF);
	public static final Color PURPLE3 = new Color(0x7F3B86FF);
	public static final Color PURPLE4 = new Color(0x7F3B86FF);
	public static final Color PURPLE5 = new Color(0x4D2352FF);

	public static void reset() {
		Colors.getColors().clear();
		Colors.put("WHITE", CustomColor.WHITE);
		Colors.put("GRIS1", CustomColor.GRIS1);
		Colors.put("GRIS2", CustomColor.GRIS2);
		Colors.put("GRIS3", CustomColor.GRIS3);
		Colors.put("GRIS4", CustomColor.GRIS4);
		Colors.put("GRIS5", CustomColor.GRIS5);
		Colors.put("BLACK", CustomColor.BLACK);

		Colors.put("GREEN1", CustomColor.GREEN1);
		Colors.put("GREEN2", CustomColor.GREEN2);
		Colors.put("GREEN3", CustomColor.GREEN3);
		Colors.put("GREEN4", CustomColor.GREEN4);

		Colors.put("BLUE1", CustomColor.BLUE1);
		Colors.put("BLUE2", CustomColor.BLUE2);
		Colors.put("BLUE3", CustomColor.BLUE3);
		Colors.put("BLUE4", CustomColor.BLUE4);
		Colors.put("BLUE5", CustomColor.BLUE5);

		Colors.put("BROWN1", CustomColor.BROWN1);
		Colors.put("BROWN2", CustomColor.BROWN2);
		Colors.put("BROWN3", CustomColor.BROWN3);
		Colors.put("BROWN4", CustomColor.BROWN4);
		Colors.put("BROWN5", CustomColor.BROWN5);
		Colors.put("BROWN6", CustomColor.BROWN6);

		Colors.put("YELLOW1", CustomColor.YELLOW1);
		Colors.put("YELLOW2", CustomColor.YELLOW2);

		Colors.put("ORANGE1", CustomColor.ORANGE1);
		Colors.put("ORANGE2", CustomColor.ORANGE2);

		Colors.put("PINK1", CustomColor.PINK1);
		Colors.put("PINK2", CustomColor.PINK2);

		Colors.put("RED1", CustomColor.RED1);
		Colors.put("RED2", CustomColor.RED2);
		Colors.put("RED3", CustomColor.RED3);

		Colors.put("PURPLE1", CustomColor.PURPLE1);
		Colors.put("PURPLE2", CustomColor.PURPLE2);
		Colors.put("PURPLE3", CustomColor.PURPLE3);
		Colors.put("PURPLE4", CustomColor.PURPLE4);
		Colors.put("PURPLE5", CustomColor.PURPLE5);
	}
}
