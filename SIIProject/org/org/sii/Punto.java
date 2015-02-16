package org.sii;

public class Punto

{

  double numeroX;
  double numeroY;

 

  // Metodo Costruttore inizializzato con due interi

  public Punto(double x , double y)
{
    numeroX = x;

    numeroY = y;

  }

  // Metodo che restituisce il valore della coordinata X

  public double daiCoordinataX()

  {

    return numeroX;

  }

  // Metodo che restituisce il valore della coordinata Y

  public double daiCoordinataY()

  {

    return numeroY;

  }

  // Metodo che permette di impostare nuove coordinate del punto

  public void imposta(double a, double b)

  {

    numeroX = a;

   numeroY = b;

  }

 

  // Metodo che permette di stampare a video le coordinate del punto

  public void stampaPunto()

  {

    System.out.print("Le coodinate del punto sono : ");

    System.out.print(numeroX + " , ");

    System.out.println(numeroY);

  }
}
