package com.gp.vaadin.task1.hotel;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HotelForm extends FormLayout {
	
    private TextField name = new TextField("Hotel name");
    private TextField address = new TextField("Hotel address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Operates from");
    private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
    private TextField url = new TextField("URL");
    private TextArea description = new TextArea("Description");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private HotelService hotelService = HotelService.getInstance();
    private Hotel hotel;
    private HotelUI hotelUI;
    private Binder<Hotel> hotelBinder = new Binder<>(Hotel.class);
    
    public HotelForm(HotelUI hotelUI) {
    	
        this.hotelUI = hotelUI;
        setSizeUndefined();
        final HorizontalLayout buttons = new HorizontalLayout(save, delete);
        
        addComponents(name, address, rating, operatesFrom, category, url, description, buttons);

        category.setItems(HotelCategory.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        hotelBinder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        hotelBinder.setBean(hotel);

        delete.setVisible(hotel.isPersisted());
        setVisible(true);
        name.selectAll();
    }

    private void delete() {
        hotelService.delete(hotel);
        hotelUI.updateList();
        setVisible(false);
    }

    private void save() {
        hotelService.save(hotel);
        hotelUI.updateList();
        setVisible(false);
    }

}
