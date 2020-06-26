public class SocketNIO {

	public static void main(String[] args) throw Exception {

		LinkedList<SocketChannel> clients = new LinkedList<>();

		ServerSocketChannel ss = ServerSocketChannel.open();
		ss.bind(new InetSocketAddress(9090));
		ss.cofigureBlocking(false);

		while (true) {
			// 延迟打印，可有可无
			Thread.sleep(1000);

			// 接收客户端连接
			SocketChannel client = ss.accept();

			if (client == null) {
				System.out.println("null client ...");
			} else {
				client.configureBlocking(false);
				int port = client.socket().getPort();
				System.out.println("client ... port: " + port);
				clients.add(client);
			}
		}

		ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

		for (SocketChannel c : clients) {
			int num = c.read(buffer);
			if (num > 0) {
				buffer.flip();
				byte[] aaa = new byte[buffer.limit()];
				buffer.get(aaa);

				String b = new String(aaa);
				System.out.println(c.socket().getPort + " : " + b);
				buffer.clear();
			}
		}
	}

}