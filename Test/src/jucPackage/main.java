package jucPackage;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello Wolrd");
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		UserService service=new UserServiceImpl();
		MyInvocationHandler handler=new MyInvocationHandler(service);
		UserService proxy=(UserService) handler.getProxy();
		proxy.add();
	}

}
