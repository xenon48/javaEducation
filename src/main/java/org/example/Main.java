import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CurrencyRate {
    private final double curs;
    public CurrencyRate(double curs) {
        this.curs = curs;
    }
    public double getCurs() {
        return curs;
    }
}

public class Main {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите команду: ");
        String command = scanner.nextLine();
        scanner.close();
        String[] inputWords = command.split(" ");
        String currency = inputWords[1].toUpperCase();
        String filePath = "classes/resources/" + currency + ".csv";
        Boolean longForecast = inputWords[2].equals("week");
        List<CurrencyRate> ratesFromCSV = readCurrencyRatesFromCSV(filePath);

        if (longForecast) {
            String answer = "\nПрогнозный курс " + currency + " на следующие 7 дней:";
            for (int i = 0; i < 7; i++) {
                double nextDayCurs = getForecastForOneDay(ratesFromCSV);
                ratesFromCSV.add(new CurrencyRate(nextDayCurs));
                answer += "\n" + decimalFormat.format(nextDayCurs);
            }
            System.out.println(answer);
        } else {
            double nextDayCurs = getForecastForOneDay(ratesFromCSV);
            System.out.println("\nПрогнозный курс " + currency + " на следующий день: " + decimalFormat.format(nextDayCurs) + "\n");
        }
    }

    private static List<CurrencyRate> readCurrencyRatesFromCSV(String filePath) {
        List<CurrencyRate> rates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4 && !(parts[0].equals("nominal"))) {
                    double curs = Double.parseDouble(parts[2].replace(',', '.'));
                    rates.add(new CurrencyRate(curs));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rates;
    }

    private static double getForecastForOneDay(List<CurrencyRate> oldRates) {
        double sum = 0;
        for (int i = (oldRates.size() - 7); i < oldRates.size(); i++) {
            sum += oldRates.get(i).getCurs();
        }
        return sum / 7;
    }
}