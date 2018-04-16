package com.gp.vaadin.task1.hotel;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@SuppressWarnings("serial")
public class HotelUI extends UI {
	
	private HotelService hotelService = HotelService.getInstance();
	private Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	private TextField filterName = new TextField();
	private TextField filterAddress = new TextField();
	private HotelForm hotelForm = new HotelForm(this);
	
    @SuppressWarnings("deprecation")
	@Override
    protected void init(VaadinRequest vaadinRequest) {
        
    	final VerticalLayout mainLayout = new VerticalLayout();    	
    	final HorizontalLayout toolbarSection = new HorizontalLayout();
    	final HorizontalLayout mainSection = new HorizontalLayout();
    	
        /*===== field: filter by name =====*/
        filterName.setPlaceholder("filter by name..");
        filterName.addValueChangeListener(e -> updateList());
        filterName.setValueChangeMode(ValueChangeMode.LAZY);
        Button clearFilterNameBtn = new Button(FontAwesome.TIMES);
        clearFilterNameBtn.setDescription("Clear the current filter");
        clearFilterNameBtn.addClickListener(e -> filterName.clear());
    	CssLayout filteringName = new CssLayout();
    	filteringName.addComponents(filterName, clearFilterNameBtn);
    	filteringName.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        /*===== field: filter by address =====*/
        filterAddress.setPlaceholder("filter by address..");
        filterAddress.addValueChangeListener(e -> updateList());
        filterAddress.setValueChangeMode(ValueChangeMode.LAZY);
        Button clearFilterAddressBtn = new Button(FontAwesome.TIMES);
        clearFilterAddressBtn.setDescription("Clear the current filter");
        clearFilterAddressBtn.addClickListener(e -> filterAddress.clear());
    	CssLayout filteringAddress = new CssLayout();
    	filteringAddress.addComponents(filterAddress, clearFilterAddressBtn);
    	filteringAddress.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP); 

        Button addHotelBtn = new Button("Add new hotel");
        addHotelBtn.addClickListener(e -> {
            hotelGrid.asSingleSelect().clear();
            hotelForm.setHotel(new Hotel());
        });

        toolbarSection.addComponents(filteringName, filteringAddress, addHotelBtn);
        
        /*===== Hotel grid =====*/
        hotelGrid.setColumns("name", "rating", "address");
        hotelGrid.addComponentColumn(hotel -> new Link("info", new ExternalResource(hotel.getUrl())));
        
        mainSection.addComponents(hotelGrid, hotelForm);
        mainSection.setSizeFull();
        hotelGrid.setSizeFull();
        mainSection.setExpandRatio(hotelGrid, 1);               
        
        mainLayout.addComponents(toolbarSection, mainSection);
        
        updateList();
        hotelForm.setVisible(false);
        
        setContent(mainLayout);
        
        hotelGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                hotelForm.setVisible(false);
            } else {
                hotelForm.setHotel(event.getValue());
            }
        });
    }

    public void updateList() {
        List<Hotel> hotels = hotelService.findAll(filterName.getValue(),filterAddress.getValue());
        hotelGrid.setItems(hotels);
    }
    
    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {
    }
}
