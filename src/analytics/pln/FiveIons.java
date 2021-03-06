package analytics.pln;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import core.correlation.CalculateMeasure;
import core.correlation.Correlation;
import core.correlation.SerialTime;
import core.entity.EntitySentiment;
import core.entity.SumarySentiment;
import core.prediction.Prediction;
import core.semantic.annotation.googlecloud.EntityAnnotation;
import core.semantic.annotation.googlecloud.SentimentEntityAnnotation;
import core.semantic.sentimentanalysis.googlecloud.SentimentAnalysis;
import core.summarization.SumyPython;
import io.db.DataClean;
import io.file.Load;
import io.file.Save;
import util.commom.Dates;
import util.commom.Files;
import util.commom.Properties;

public class FiveIons {

	private static final String HOST = "localhost";
	private static final String DATABASENAME = "db_news_brazil";
	private static Save save = new Save();
	private static EntityAnnotation entityAnnotation = new EntityAnnotation(HOST, DATABASENAME, "mentions",
			"documents");
	private static SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(HOST, DATABASENAME, "documents");
	private static SentimentEntityAnnotation sentimentEntityAnnotation = new SentimentEntityAnnotation(HOST,
			DATABASENAME, "mentions", "documents");
	private static Load load = new Load();
	private static SerialTime serialTime = new SerialTime(HOST, DATABASENAME, "mentions");
	private static CalculateMeasure calculateMeasure = new CalculateMeasure(HOST, DATABASENAME, "mentions");
	private static Correlation correlation = new Correlation(HOST, DATABASENAME, "mentions", "externalfile");
	private static SumyPython sumy = new SumyPython();
	private static Prediction prediction = new Prediction(HOST, DATABASENAME, "mentions", "externalfile");
	private static DataClean dataClean = new DataClean(HOST, DATABASENAME, "mentions", "externalfile");

	public static void main(String[] args) throws InterruptedException {

		try {
			System.out.println("Entity Annotation");
			entityAnnotation.analyzeEntitiesText();

			System.out.println("Sentiment Analysis");
			sentimentAnalysis.analyzeSentimentText();

			boolean clear = false;
			if(clear) {
				System.out.println("Clear Datas - Linked Data");
				dataClean.joinDocuments("real");
			}
						
			System.out.println("Entity Sentiment Analysis");
			sentimentEntityAnnotation.entitySentimentText();
			
			System.exit(0);
			
			
			System.out.println("SerialTime");
			serialTime.generationSerialTime();
		
			System.out.println("Calculate Metric");
			calculateMeasure.summarizationMetric();
			
			System.out.println("Correlation Metric");
			correlation.calculeCorrelation("real", "real", 200, .52);

			System.out.println("Perdiction");
			prediction.calculePrediction("real", "real", 200, .5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void compositeMetric() {
		Set<String> names = new HashSet<>();

		names.add("AfD");
		// names.add("CDU");
		// names.add("FDP");
		// names.add("GRUNE");
		// names.add("LINKE");
		// names.add("SPD");

		String pathC = "C:\\Users\\Home\\Dropbox\\Mestrado\\Dissertação\\Dados\\Research\\Metric LSA\\Fors\\C\\";
		String pathP = "C:\\Users\\Home\\Dropbox\\Mestrado\\Dissertação\\Dados\\Research\\Metric LSA\\Fors\\P\\";
		String pathE = "C:\\Users\\Home\\Dropbox\\Mestrado\\Dissertação\\Dados\\Research\\Metric LSA\\Fors\\E\\";

		Hashtable<String, List<SumarySentiment>> list = new Hashtable<>();

		list.put("AfD-C", load.getSerialTimeCompact(pathC, "AfD.csv"));
		list.put("AfD-P", load.getSerialTimeCompact(pathP, "AfD.csv"));
		list.put("AfD-E", load.getSerialTimeCompact(pathE, "AfD.csv"));

		// list.put("CDU-C", load.getSerialTimeCompact(pathC, "CDU.csv"));
		// list.put("CDU-P", load.getSerialTimeCompact(pathP, "CDU.csv"));
		// list.put("CDU-E", load.getSerialTimeCompact(pathE, "CDU.csv"));

		// list.put("FDP-C", load.getSerialTimeCompact(pathC, "FDP.csv"));
		// list.put("FDP-P", load.getSerialTimeCompact(pathP, "FDP.csv"));
		// list.put("FDP-E", load.getSerialTimeCompact(pathE, "FDP.csv"));

		// list.put("GRUNE-C", load.getSerialTimeCompact(pathC, "GRUNE.csv"));
		// list.put("GRUNE-P", load.getSerialTimeCompact(pathP, "GRUNE.csv"));
		// list.put("GRUNE-E", load.getSerialTimeCompact(pathE, "GRUNE.csv"));

		// list.put("LINKE-C", load.getSerialTimeCompact(pathC, "LINKE.csv"));
		// list.put("LINKE-P", load.getSerialTimeCompact(pathP, "LINKE.csv"));
		// list.put("LINKE-E", load.getSerialTimeCompact(pathE, "LINKE.csv"));

		// list.put("SPD-C", load.getSerialTimeCompact(pathC, "SPD.csv"));
		// list.put("SPD-P", load.getSerialTimeCompact(pathP, "SPD.csv"));
		// list.put("SPD-E", load.getSerialTimeCompact(pathE, "SPD.csv"));

		serialTime.sComposite(names, list);
	}

	public static void summarizationText(String fileName, String tittle, String date, String text) {
		System.out.println("Start Summarization");

		if (!util.commom.Files.existsFile(
				"C:\\Users\\Vanderson\\Dropbox\\Mestrado\\Dissertação\\Dados\\DMAlemanhaSumm\\", fileName, "summ")) {
			save.setExtension("summ");
			save.setFileName(fileName);
			save.setPath("C:\\Users\\Vanderson\\Dropbox\\Mestrado\\Dissertação\\Dados\\DMAlemanhaSumm");
			JSONObject summary = sumy.summarizationLuhn(tittle + "\r\n" + text, date);
			save.save(summary);
			System.out.println(fileName + ".summ ADICIONADO!");
		}

		System.out.println("End Summarization");
	}

	public static void prediction() throws InterruptedException {
		System.out.println("Start Prediction");

		//prediction.gennerationARFF();

		System.out.println("End Prediction");
	}

	public static void correlationSeries() {
		System.out.println("Start Correlation Serial Time");
		List<String> listNames = Files.getAllFileNames(Properties.getProperty("pathCorrelation"));

		for (String fileName : listNames) {
			double[] serieInternal = load.getSerialTimeCompact(Properties.getProperty("pathCorrelation"), fileName, 1);
			double[] serieCurrent = load.getSerialTimeCompact(Properties.getProperty("fileExternalData"), fileName, 4);

			double correlation = serialTime.calculateCorrelation(serieInternal, serieCurrent);

			System.out.println("Entidade: " + fileName + " Valor Correlacionado: " + correlation);
		}

		System.out.println("End Correlation Serial Time");
	}

	public static void summarizationMetric(String InitialDate, String EndDate) {
		System.out.println("Start Calculate Metrics");

		List<String> listFiles = Files.getAllFileNames(Properties.getProperty("pathSerialTimeMetric"));

		for (String fileName : listFiles) {
			if (fileName.contains(".ini"))
				continue;

			Hashtable<Long, EntitySentiment> analise = load
					.getSerialTime(Properties.getProperty("pathSerialTimeMetric") + File.separator + fileName);

			serialTime.summarizationMetric(Dates.dateTime(InitialDate), Dates.dateTime(EndDate), analise, "C",
					fileName.split("\\.")[0]);
		}

		System.out.println("End Calculate Metrics");
	}

	public static void generation7uplas(String InitialDate, String EndDate) {
		System.out.println("Start Generation 7uplas");

		JSONArray entitiesSentiments = load.getEntitiesSentiment();
		for (int i = 0; i < entitiesSentiments.size(); i++) {
			serialTime.parse((JSONObject) entitiesSentiments.get(i));
		}

		serialTime.generationSerie(Dates.dateTime(InitialDate), Dates.dateTime(EndDate));

		System.out.println("End Generation 7uplas");
	}
}
