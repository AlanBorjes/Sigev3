package mx.edu.utez.sigev.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ImagenUtileria {
	public static String guardarImagen(MultipartFile multipartFile, String ruta) {
		String nombreImagen = multipartFile.getOriginalFilename();
		System.err.println(nombreImagen);
		try {
			System.err.println(ruta);
			String rutaArchivo = ruta + "/" + nombreImagen;
			System.err.println(rutaArchivo);
			File imagen = new File(rutaArchivo);
			multipartFile.transferTo(imagen);
			return nombreImagen;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return "null";
		}
	}
}
