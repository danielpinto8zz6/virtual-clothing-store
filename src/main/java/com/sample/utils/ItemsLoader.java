package com.sample.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sample.Constants.Gender;
import com.sample.model.Item;

public class ItemsLoader {
	public static List<Item> fromFile(File file) {
		List<Item> items = new ArrayList<>();
		List<String> allLines;

		try {
			allLines = Files.readAllLines(file.toPath());
		} catch (IOException e) {
			return null;
		}

		for (String line : allLines) {
			Item item = parseItem(line);
			if (item != null) {
				items.add(item);
			}
		}

		return items.isEmpty() ? null : items;
	}

	public static Item parseItem(String line) {
		List<String> props = Arrays.asList(line.split(","));
		if (props.size() == 3) {
			String name = props.get(0).trim();
			Gender gender = (props.get(1) == "male" || props.get(1) == "masculino") ? Gender.MALE : Gender.FEMALE;
			double price = Double.parseDouble(props.get(2));

			return new Item(name, gender, price);
		}
		return null;
	}
}