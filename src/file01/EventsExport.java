//package file01;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//import domain.Events;
//
//public class EventsExport {
//	/**
//	 *
//	 * パラメータで渡されたEvents型のリストをfileNameに指定されている
//	 * ファイルにカンマ区切りで出力する
//	 *
//	 * @param　　	eventsList　	EventsクラスList
//	 * 				fileName		パスを含むファイル名
//	 *
//	 * @return boolean
//	 */
//
//	public boolean writeEvents(List<Events> eventsList, String fileName) {
//		Path path = Paths.get(fileName);
//		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//
//			for (Events events : eventsList) {
//				writer.append(events.getTitle() + "," + events.getPlace());
//				writer.newLine();
//
//			}
//			writer.close();
//			return true;
//		} catch (IOException ex) {
//			System.err.println(ex);
//			return false;
//		}
//	}
//}
