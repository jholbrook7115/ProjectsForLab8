public class RecordTestJava {

	public static class ARecord {
		public int x;
		public double y;
		public int[] z = new int[10];
	
	}

	public static class BRecord {
		public ARecord r = new ARecord();
	}

	public static ARecord aRecordVariable = new ARecord();
	public static BRecord bRecordVariable = new BRecord();
	public static int w;
	
	public static void main(String[] args) {
		aRecordVariable.x = 5;
		aRecordVariable.y = 10.5;
		aRecordVariable.z[3] = 99;
		w = aRecordVariable.z[3];
		bRecordVariable.r.x = 3;
	}
}
