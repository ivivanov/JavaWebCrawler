public class Program {
	public static void main(String[] args) {
		String root = "http://fmi.wikidot.com";
		Crowler cr = new Crowler();
		String result = cr.crowl(root, "Докажете, че:");
		System.out.println(result);
	}
}
