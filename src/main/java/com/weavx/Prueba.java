package com.weavx;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.weavx.web.model.Data;
import com.weavx.web.model.DataItem;
import com.weavx.web.model.Item;

public class Prueba {

	@SuppressWarnings("serial")
	public static void main(String[] args) throws Exception {
		
		URI url = Prueba.class.getResource("/data.json").toURI();
		byte[] jsonData = Files.readAllBytes(Paths.get(url));

		ObjectMapper objectMapper = new ObjectMapper();
		Data obj = objectMapper.readValue(jsonData, Data.class);
		List<Item> data = obj.getData();

		long QUANTITY = data.size();
		double TOTAL = data.stream().mapToDouble(Item::getAmount).sum();
		long DONORS = data.stream().filter(x -> x.getScheduled() == 0).mapToInt(Item::getScheduled).count();
		long RECURRINGS = data.stream().filter(x -> x.getScheduled() == 1).mapToInt(Item::getScheduled).count();
		double DONORS_INCOME = data.stream().filter(x -> x.getScheduled() == 0).mapToDouble(Item::getAmount).sum();
		double RECURRINGS_INCOME = data.stream().filter(x -> x.getScheduled() == 1).mapToDouble(Item::getAmount).sum();
		double DONORS_QUANTITY_PERCENTAGE = DONORS / (double) QUANTITY * 100;
		double DONORS_AMOUNT_PERCENTAGE = DONORS_INCOME / (double) TOTAL * 100;
		double RECURRINGS_QUANTITY_PERCENTAGE = RECURRINGS / (double) QUANTITY * 100;
		double RECURRINGS_AMOUNT_PERCENTAGE = RECURRINGS_INCOME / (double) TOTAL * 100;
		
		Map<Object, DataItem> Totals = new HashMap<Object, DataItem>() {
			{
				put("DONORS", new DataItem("DONORS", DONORS, DONORS_INCOME, DONORS_QUANTITY_PERCENTAGE, DONORS_AMOUNT_PERCENTAGE));
				put("RECURRINGS", new DataItem("RECURRINGS", RECURRINGS, RECURRINGS_INCOME, RECURRINGS_QUANTITY_PERCENTAGE, RECURRINGS_AMOUNT_PERCENTAGE));
			};
		};

		Map<String, Map<Object, DataItem>> summary = new HashMap<String, Map<Object, DataItem>>() {
			{
				put("TOTALS", Totals);
			};
		};

		String parameter = "D";
		
		Map<String, Function<Item, Object>> dateGroups = new HashMap<String, Function<Item, Object>>() {
			{
				put("T", Item::getSource);
				put("H", Item::getSource);
				put("D", Item::getSource_type);
				put("W", Item::getContinentName);
				put("M", Item::getCountryName);
			};
		};
		
		Function<Item, Object> detailFunction = dateGroups.get(parameter);
		
		Map<String, Function<Item, Object>> groups = new HashMap<String, Function<Item, Object>>() {
			{
				put("sources", Item::getSource);
				put("sources_type", Item::getSource_type);
				put("continents", Item::getContinentName);
				put("countries", Item::getCountryName);
				put("details", detailFunction);
			};
		};

		groups.forEach((group, clasiffier) -> {
			Map<Object, DataItem> result = data.stream()
					.collect(Collectors.groupingBy(clasiffier,
							Collectors.collectingAndThen(Collectors.summarizingDouble(Item::getAmount),
									source -> new DataItem(
											null, 
											source.getCount(), 
											source.getSum(),
											source.getCount() / (double) QUANTITY * 100,
											source.getSum() / (double) TOTAL * 100))));
			result.forEach((label, item) -> { item.setLabel(label.toString()); });
			summary.put(group, result);
		});
		String json = objectMapper.writeValueAsString(summary);
		System.out.println(json);
	}
}
