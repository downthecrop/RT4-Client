import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static71 {

	@OriginalMember(owner = "client!fk", name = "e", descriptor = "I")
	public static int anInt1885;

	@OriginalMember(owner = "client!fk", name = "j", descriptor = "[[I")
	public static int[][] anIntArrayArray10;

    @OriginalMember(owner = "client!fk", name = "q", descriptor = "Lclient!uc;")
	public static MouseWheel mouseWheel;

	@OriginalMember(owner = "client!fk", name = "g", descriptor = "Z")
	public static boolean fogEnabled = true;

	@OriginalMember(owner = "client!fk", name = "k", descriptor = "[I")
	public static final int[] anIntArray147 = new int[14];

	@OriginalMember(owner = "client!fk", name = "a", descriptor = "(IB)Lclient!h;")
	public static ObjType get(@OriginalArg(0) int arg0) {
		@Pc(6) ObjType local6 = (ObjType) Static27.aClass99_4.get((long) arg0);
		if (local6 != null) {
			return local6;
		}
		@Pc(25) byte[] local25 = Static167.aClass153_61.getFile(Static18.method554(arg0), Static247.method4247(arg0));
		local6 = new ObjType();
		local6.anInt2354 = arg0;
		if (local25 != null) {
			local6.decode(new Buffer(local25));
		}
		local6.method1826();
		if (local6.certificateTemplate != -1) {
			local6.generateCertificate(get(local6.certificateLink), get(local6.certificateTemplate));
		}
		if (local6.lentTemplate != -1) {
			local6.method1823(get(local6.lentLink), get(local6.lentTemplate));
		}
		if (!Static240.aBoolean276 && local6.members) {
			local6.name = LocalizedText.MEMBERS_OBJECT;
			local6.team = 0;
			local6.inventoryOps = Static143.aClass100Array104;
			local6.stockMarket = false;
			local6.ops = Static269.aClass100Array87;
		}
		Static27.aClass99_4.put(local6, (long) arg0);
		return local6;
	}

	@OriginalMember(owner = "client!fk", name = "a", descriptor = "(B)V")
	public static void method1441() {
		Static279.aClass99_38.method3103();
		Static56.models.method3103();
	}

	@OriginalMember(owner = "client!fk", name = "a", descriptor = "([BIZ)I")
	public static int method1442(@OriginalArg(0) byte[] arg0, @OriginalArg(1) int arg1) {
		return Static169.crc32(0, arg1, arg0);
	}

	@OriginalMember(owner = "client!fk", name = "b", descriptor = "(IB)V")
	public static void method1443() {
		Static83.aClass99_3.method3102(5);
	}

	@OriginalMember(owner = "client!fk", name = "b", descriptor = "(I)V")
	public static void method1444() {
		for (@Pc(7) int local7 = -1; local7 < Static267.anInt5774; local7++) {
			@Pc(21) int local21;
			if (local7 == -1) {
				local21 = 2047;
			} else {
				local21 = Static105.anIntArray256[local7];
			}
			@Pc(31) Player local31 = Static159.players[local21];
			if (local31 != null) {
				Static263.method4514(local31.getSize(), local31);
			}
		}
	}

	@OriginalMember(owner = "client!fk", name = "c", descriptor = "(I)V")
	public static void transmitVerifyId() {
		Static6.outboundBuffer.p1isaac(177);
		Static6.outboundBuffer.p2(Static189.anInt4443);
	}
}
