package tickets;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import train.Stop;
import utilities.Time;

public class Ticket implements Serializable{

    protected int id;
    protected int trainId;
    protected int price;

    protected String passengerName;
    protected Stop from;
    protected Stop to;
    protected Time timeOfLeave;
    protected Time timeOfArrival;

    public Ticket(int id, int trainId, int price, String passengerName, Stop from, Stop to) {
        
        this.id = id;
        this.trainId = trainId;
        this.price = price;
        this.passengerName = passengerName;
        this.from = from;
        this.to = to;
        this.timeOfLeave = from.getLeave();
        this.timeOfArrival = to.getArrive();
    }

    public int getSeat(){return id;}
    public int getTrainId(){return trainId;}
    public int getPrice(){return price;}
    public String getPassengerName(){return passengerName;}

    public String getTicketType() {
        return "Menetjegy";
    }

    @Override
    public String toString() {
        return getTicketType() + " | " + timeOfLeave + " | " + from + " -> " + to + " (" + passengerName + ")";
    }

    public void writeToHtml() {
        String cleanName = passengerName.replaceAll("\\s+", "_");
        String filename = "Ticket_" + cleanName + "_" + id + ".html";

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n");
            writer.write("<meta charset='UTF-8'>\n");
            writer.write("<style>\n");
            writer.write("  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0f0f0; display: flex; justify-content: center; padding-top: 50px; }\n");
            writer.write("  .ticket { background-color: white; width: 400px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); overflow: hidden; border-left: 10px solid #2ecc71; }\n");
            writer.write("  .header { background-color: #333; color: white; padding: 20px; text-align: center; }\n");
            writer.write("  .header h1 { margin: 0; font-size: 24px; text-transform: uppercase; letter-spacing: 2px; }\n");
            writer.write("  .content { padding: 20px; }\n");
            writer.write("  .row { margin-bottom: 15px; border-bottom: 1px dashed #ddd; padding-bottom: 10px; }\n");
            writer.write("  .label { font-size: 12px; color: #888; text-transform: uppercase; display: block; margin-bottom: 5px; }\n");
            writer.write("  .value { font-size: 18px; color: #333; font-weight: bold; }\n");
            writer.write("  .footer { background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #666; }\n");
            writer.write("  .price { color: #27ae60; font-size: 22px; }\n");
            writer.write("</style>\n</head>\n<body>\n");

            writer.write("<div class='ticket'>\n");
            writer.write("  <div class='header'><h1>Vasúti Menetjegy</h1></div>\n");
            writer.write("  <div class='content'>\n");
            
            writeRow(writer, "Utas Neve", passengerName);
            writeRow(writer, "Viszonylat", this.from.getName() + "(" + this.from.getLeave() + ") ➝ " + this.to.getName() + "(" + this.to.getArrive() + ")");
            writeRow(writer, "Vonatszám", String.valueOf(trainId));

            writer.write(getExtraDetails()); 

            writer.write("    <div class='row' style='border-bottom: none;'>\n");
            writer.write("      <span class='label'>Ár</span>\n");
            writer.write("      <span class='value price'>" + price + " Ft</span>\n");
            writer.write("    </div>\n");

            writer.write("  </div>\n");

            writer.write("  <div class='footer'>Köszönjük, hogy minket választott!<br>Jó utat kíván a Malulukai Állami Vasúttársaság</div>\n");
            writer.write("</div>\n</body>\n</html>");

            System.out.println("HTML jegy generálva: " + filename);

        } catch (IOException e) {
            System.err.println("Hiba a fájl írásakor: " + e.getMessage());
        }
    }
    protected void writeRow(FileWriter w, String label, String value) throws IOException {
        w.write("    <div class='row'>\n");
        w.write("      <span class='label'>" + label + "</span>\n");
        w.write("      <span class='value'>" + value + "</span>\n");
        w.write("    </div>\n");
    }

    protected String getExtraDetails() {
        return "    <div class='row'>\n" +
               "      <span class='label'>Típus</span>\n" +
               "      <span class='value'>Helyjegy nélküli menetjegy</span>\n" +
               "    </div>\n";
    }
}

