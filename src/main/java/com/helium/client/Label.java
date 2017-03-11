package com.helium.client;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.helium.api.HeliumApi;
import com.helium.model.Metadata;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Label implements HasMetadata, HasSensors {

    private final HeliumApi api;
    private com.helium.model.Label model;

    protected Label(HeliumApi api, com.helium.model.Label model) {
        this.api = api;
        this.model = model;
    }

    public static Label createLabel(HeliumApi api, String labelName) throws IOException {
        com.helium.model.Label newModel =
                api.label.createLabel(com.helium.model.Label.newLabel(labelName))
                .execute().body().get();
        return new Label(api, newModel);
    }

    public static List<Label> getLabels(HeliumApi api) throws IOException {
        List<com.helium.model.Label> labels = api.label.labels().execute().body().get();
        List<Label> clientLabels = new ArrayList<>();
        for (com.helium.model.Label label : labels) {
            clientLabels.add(new Label(api, label));
        }
        return clientLabels;
    }

    public static Optional<Label> lookupLabel(HeliumApi api, String labelId) throws IOException {
        Response<JSONAPIDocument<com.helium.model.Label>> labelResponse = api.label.label(labelId).execute();
        if (labelResponse.isSuccessful()) {
            return Optional.of(new Label(api, labelResponse.body().get()));
        }
        else {
            return Optional.empty();
        }
    }

    public Label setName(String name) throws IOException {
        model.setName(name);
        return new Label(api, api.label.updateLabel(model).execute().body().get());
    }

    public void delete() throws IOException {
        api.label.deleteLabel(model.id()).execute();
    }

    public String id() {
        return model.id();
    }

    @Override
    public List<Sensor> sensors() throws IOException {
        Response<JSONAPIDocument<List<com.helium.model.Sensor>>> response =
                api.label.labelSensors(model.id()).execute();
        List<com.helium.model.Sensor> sensorModels = response.body().get();
        List<Sensor> sensors = new ArrayList<>();
        for (com.helium.model.Sensor model : sensorModels) {
            sensors.add(new Sensor(api, model));
        }
        return sensors;
    }

    public Label addSensor(Sensor sensor) throws IOException {
       return addSensors(Arrays.asList(sensor));
    }

    public Label addSensors(List<Sensor> sensors) throws IOException {
        List<com.helium.model.Sensor> sensorModels = new ArrayList<>();
        for(Sensor sensor : sensors) {
            sensorModels.add(sensor.model());
        }
        api.label.addSensors(id(), sensorModels).execute().body().get();
        return lookupLabel(api, id()).get();
    }

    public Label replaceSensors(List<Sensor> sensors) throws IOException {
        List<com.helium.model.Sensor> sensorModels = new ArrayList<>();
        for(Sensor sensor : sensors) {
            sensorModels.add(sensor.model());
        }
        api.label.replaceSensors(id(), sensorModels).execute().body().get();
        return lookupLabel(api, id()).get();
    }

    public Label clearSensors() throws IOException {
        return replaceSensors(new ArrayList<>());
    }

    public Label removeSensor(Sensor sensor) throws IOException {
        return removeSensors(Arrays.asList(sensor));
    }

    public Label removeSensors(List<Sensor> sensors) throws IOException {
        List<com.helium.model.Sensor> sensorModels = new ArrayList<>();
        for(Sensor sensor : sensors) {
            sensorModels.add(sensor.model());
        }
        api.label.removeSensors(id(), sensorModels).execute().body().get();
        return lookupLabel(api, id()).get();
    }

    public List<Element> elements() throws IOException {
        Response<JSONAPIDocument<List<com.helium.model.Element>>> response =
                api.label.labelElements(model.id()).execute();
        List<com.helium.model.Element> elementModels = response.body().get();
        List<Element> elements = new ArrayList<>();
        for (com.helium.model.Element model : elementModels) {
            elements.add(new Element(api, model));
        }
        return elements;
    }

    public Label addElement(Element element) throws IOException {
        return addElements(Arrays.asList(element));
    }

    public Label addElements(List<Element> elements) throws IOException {
        List<com.helium.model.Element> elementModels = new ArrayList<>();
        for(Element element : elements) {
            elementModels.add(element.model());
        }
        api.label.addElements(id(), elementModels).execute().body().get();
        return lookupLabel(api, id()).get();
    }

    public Label replaceElements(List<Element> elements) throws IOException {
        List<com.helium.model.Element> elementModels = new ArrayList<>();
        for(Element element : elements) {
            elementModels.add(element.model());
        }
        api.label.replaceElements(id(), elementModels).execute().body().get();
        return lookupLabel(api, id()).get();
    }

    public Label clearElements() throws IOException {
        return replaceElements(new ArrayList<>());
    }

    public Label removeElement(Element element) throws IOException {
        return removeElements(Arrays.asList(element));
    }

    public Label removeElements(List<Element> elements) throws IOException {
        List<com.helium.model.Element> elementModels = new ArrayList<>();
        for(Element element : elements) {
            elementModels.add(element.model());
        }
        api.label.removeElements(id(), elementModels).execute().body().get();
        return lookupLabel(api, id()).get();
    }

    @Override
    public String toString() {
        return model.toString();
    }


    @Override
    public Metadata metadata() throws IOException {
        return api.label.labelMetadata(id()).execute().body();
    }

    @Override
    public Label updateMetadata(Metadata metadata) throws IOException {
        api.label.updateLabelMetadata(id(), metadata).execute().body();
        return this;
    }

    @Override
    public Label replaceMetadata(Metadata metadata) throws IOException {
        api.label.replaceLabelMetadata(id(), metadata).execute().body();
        return this;
    }
}
