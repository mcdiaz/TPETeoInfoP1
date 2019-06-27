import java.util.Scanner;

import javax.swing.JFileChooser;

public class Interfaz {

	public static void main(String[] args) {
		Scanner entrada=new Scanner(System.in);
        System.out.println("Que desea hacer? 1-Comprimir 2-Descomprimir 3-Sacar Ruido");
        int num=entrada.nextInt();
		
        if(num==1) {
			JFileChooser ventanita=new JFileChooser();
			ventanita.showOpenDialog(ventanita);
			String ruta=ventanita.getSelectedFile().getAbsolutePath();//obtiene la ruta del archivo selecionado
			Imagen imagen= new Imagen(ruta);
			imagen.Dividir();
		//	imagen.comprimir((float) 3.4);
		}
        else
        	if(num==2) {
        		Decodificacion d = new Decodificacion();
        	}
        	else
        		if(num==3) {
        			Canal c = new Canal();
        			c.calcularRuido();
        		}

	}

}
