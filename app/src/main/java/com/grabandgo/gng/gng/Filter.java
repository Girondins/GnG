package com.grabandgo.gng.gng;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Category filter.
 */
public class Filter implements Serializable {

    private static final long serialVersionUID = -8821277811871324512L;
    private LinkedList<Category> filters = new LinkedList<Category>();

    public Filter() {
        filters.add(new Category("Allergi", "Skaldjur"));
        filters.add(new Category("Allergi", "Gluten"));
        filters.add(new Category("Allergi", "Nötter"));
        filters.add(new Category("Allergi", "Laktos"));
        filters.add(new Category("Allergi", "Ägg"));
        filters.add(new Category("Allergi", "Soya"));
        filters.add(new Category("Asiatisk", "Japansk"));
        filters.add(new Category("Asiatisk", "Thailändsk"));
        filters.add(new Category("Asiatisk", "Kinesisk"));
        filters.add(new Category("Asiatisk", "Koreansk"));
        filters.add(new Category("Asiatisk", "Vietnamesisk"));
        filters.add(new Category("Asiatisk", "Mongolisk"));
        filters.add(new Category("Orientalisk", "Turkisk"));
        filters.add(new Category("Orientalisk", "Libanesisk"));
        filters.add(new Category("Orientalisk", "Irakisk"));
        filters.add(new Category("Orientalisk", "Persisk"));
        filters.add(new Category("Orientalisk", "Övriga"));
        filters.add(new Category("Latin Amerikansk", "Mexikanskt"));
        filters.add(new Category("Latin Amerikansk", "Brasilianskt"));
        filters.add(new Category("Latin Amerikansk", "Argentinskt"));
        filters.add(new Category("Latin Amerikansk", "Kubansk"));
        filters.add(new Category("Europeisk", "Skandinavisk"));
        filters.add(new Category("Europeisk", "Italiensk"));
        filters.add(new Category("Europeisk", "Fransk"));
        filters.add(new Category("Europeisk", "Grekisk"));
        filters.add(new Category("Europeisk", "Spansk"));
        filters.add(new Category("Europeisk", "Balkan"));
        filters.add(new Category("Europeisk", "Övriga"));
        filters.add(new Category("None", "Afrikansk"));
        filters.add(new Category("None", "Indisk"));
        filters.add(new Category("None", "Amerikansk"));
        filters.add(new Category("None", "Australiensk"));
        filters.add(new Category("None", "Husmanskost"));
        filters.add(new Category("None", "Fine-Dining"));
        filters.add(new Category("Mer", "Steak"));
        filters.add(new Category("Mer", "Vegetarisk"));
        filters.add(new Category("Mer", "Korv"));
        filters.add(new Category("Mer", "Fisk"));
        filters.add(new Category("Mer", "Sushi"));
        filters.add(new Category("Mer", "Pizza"));
        filters.add(new Category("Mer", "Sandwich"));
        filters.add(new Category("Mer", "Pasta"));
        filters.add(new Category("Mer", "Kebab"));
        filters.add(new Category("Mer", "Hamburgare"));
        filters.add(new Category("Mer", "Tapas"));
        filters.add(new Category("Mer", "Buffe"));
        filters.add(new Category("Mer", "Smoothie/Juice -bar"));
        filters.add(new Category("Mer", "Café"));
        filters.add(new Category("Mer", "Serveringstillstånd"));
    }

    public boolean getCategoryStatus(int i) {
        return filters.get(i).getStatus();
    }

    public void setCategoryTrue(String category) {
        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i).equals(category)) {
                filters.get(i).setStatus(true);
            }
        }
    }

    public void setCategoryFalse(String category) {
        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i).equals(category)) {
                filters.get(i).setStatus(false);
            }
        }
    }

    public Category getIndex(int i) {
        return filters.get(i);
    }

    public int length() {
        return filters.size();
    }
}