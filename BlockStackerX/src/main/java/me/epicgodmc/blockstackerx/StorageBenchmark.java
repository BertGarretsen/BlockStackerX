package me.epicgodmc.blockstackerx;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import me.epicgodmc.blockstackerx.util.StackerLocation;
import me.imspooks.baos.Baos;
import me.imspooks.baos.BaosDeserializer;
import me.imspooks.baos.BaosSerializer;
import me.imspooks.baos.io.BaosInputStream;
import me.imspooks.baos.io.BaosOutputStream;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bert on 23 Sep 2021
 * Copyright © EpicGodMC
 */
public class StorageBenchmark {


    private static final Path BYTE_TARGET = Paths.get("C:\\Users\\Bert Garretsen\\Desktop\\BenchMark").resolve("Byte.dat");
    private static final Path JSON_TARGET = Paths.get("C:\\Users\\Bert Garretsen\\Desktop\\BenchMark").resolve("Json.json");


    private static long jsonWriteSpeed, byteWriteSpeed;
    private static long jsonReadSpeed, byteReadSpeed;

    private static GsonBuilder BUILDER = new GsonBuilder()
            .registerTypeAdapter(StorageBenchmark.DataObject.class, new StorageBenchmark.DataObject())
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.hasModifier(Modifier.STATIC) || f.hasModifier(Modifier.TRANSIENT);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .disableHtmlEscaping();

    public static Gson GSON;

    static {
        GSON = BUILDER.setPrettyPrinting().create();
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        createFiles();


        if (jsonWriteSpeed != 0) {
            System.out.println("Json Save Speed: " + jsonWriteSpeed);
        }
        if (byteWriteSpeed != 0) {
            System.out.println("Byte Save Speed: " + byteWriteSpeed);
        }

        byte[] jsonData = Files.readAllBytes(JSON_TARGET);
        byte[] byteData = Files.readAllBytes(BYTE_TARGET);


        int iterations = 10000;


        System.out.println("Running Byte Test: (" + iterations + " iterations)");
        System.out.println();
        runTest(byteData, iterations, "byte");

        System.out.println();
        System.out.println("----------------");
        System.out.println();

        System.out.println("Running Json Test: (" + iterations + " iterations)");
        System.out.println();
        runTest(jsonData, iterations, "json");
    }

    private static void runTest(byte[] data, int amount, String type) throws IOException {
        long lowest = Long.MAX_VALUE;
        long highest = 0;
        long total = 0;

        for (int i = 0; i < amount; i++) {
            long time = type.equalsIgnoreCase("byte") ? testByteRead(data) : testJsonRead(data);

            total += time;
            lowest = Math.min(lowest, time);
            highest = Math.max(highest, time);
        }

        System.out.println("Fastest time was " + msNow(lowest) + " ms.");
        System.out.println("Slowest time was " + msNow(highest) + " ms.");
        System.out.println("Total time was " + msNow(total) + " ms.");
        System.out.println("Average time was " + msNow(total / amount) + " ms.");
    }


    private static void createFiles() {
        try {
            boolean writeJson = false;
            boolean writeByte = false;
            if (!Files.exists(BYTE_TARGET)) {
                Files.createFile(BYTE_TARGET);
                writeByte = true;
            }
            if (!Files.exists(JSON_TARGET)) {
                Files.createFile(JSON_TARGET);
                writeJson = true;
            }

            if (writeByte && writeJson) writeData(2);
            if (writeByte) writeData(1);
            if (writeJson) writeData(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long testJsonRead(byte[] bytes) {
        long start = System.nanoTime();

        String json = new String(bytes, StandardCharsets.UTF_8);

        List<DataObject> list = GSON.fromJson(json, new TypeToken<List<DataObject>>() {
        }.getType());

        return System.nanoTime() - start;
    }


    private static long testByteRead(byte[] bytes) throws IOException {
        long start = System.nanoTime();

        List<DataObject> cache = new ArrayList<>();

        Baos.read(bytes, in -> {

            int length = in.readInt();
            for (int i = 0; i < length; i++) {
                DataObject dataObject = new DataObject();
                dataObject.deserialize(in);

                cache.add(dataObject);
            }
        });

        return System.nanoTime() - start;
    }


    // 1 = byte
    // 0 = json
    // 2 = both
    private static void writeData(int i) {
        List<DataObject> data = new ArrayList<>();

        for (int j = 0; j < 100; j++) {
            data.add(new DataObject());
        }


        if (i == 2) {
            writeJson(data);
            writeByte(data);
        }
    }

    private static void writeJson(List<DataObject> data) {
        long start = System.nanoTime();

        try {
            Files.write(JSON_TARGET, GSON.toJson(data).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonWriteSpeed = System.nanoTime() - start;
    }

    private static void writeByte(List<DataObject> dataList) {
        long start = System.nanoTime();

        try {
            byte[] bytes = Baos.write(out ->
            {
                out.writeInt(dataList.size());

                for (DataObject data : dataList) {
                    data.serialize(out);
                }
            });
            Files.write(BYTE_TARGET, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteWriteSpeed = System.nanoTime() - start;

    }

    public static String ms(long now) {
        return String.format("%.3f", (double) (System.nanoTime() - now) / 1_000_000);
    }

    public static String msNow(long time) {
        return String.format("%.3f", (double) time / 1_000_000);
    }


    @Data
    public static class DataObject implements BaosSerializer, BaosDeserializer, JsonDeserializer<DataObject>, JsonSerializer<DataObject> {

        private UUID u1, u2, u3;
        private StackerLocation loc1, loc2, loc3;

        public DataObject() {
            u1 = UUID.randomUUID();
            u2 = UUID.randomUUID();
            u3 = UUID.randomUUID();

            loc1 = StackerLocation.randomLocation();
            loc2 = StackerLocation.randomLocation();
            loc3 = StackerLocation.randomLocation();
        }

        @Override
        public void deserialize(BaosInputStream in) throws IOException {
            this.u1 = in.readUUID();
            this.u2 = in.readUUID();
            this.u3 = in.readUUID();

            this.loc1 = new StackerLocation(in);
            this.loc2 = new StackerLocation(in);
            this.loc3 = new StackerLocation(in);
        }

        @Override
        public void serialize(BaosOutputStream out) throws IOException {
            out.writeUUID(u1);
            out.writeUUID(u2);
            out.writeUUID(u3);

            loc1.serialize(out);
            loc2.serialize(out);
            loc3.serialize(out);
        }

        @Override
        public DataObject deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();

                UUID u1 = UUID.fromString(object.get("u1").getAsString());
                UUID u2 = UUID.fromString(object.get("u2").getAsString());
                UUID u3 = UUID.fromString(object.get("u3").getAsString());


                StackerLocation loc1 = StackerLocation.deserialize(object.get("loc1"));
                StackerLocation loc2 = StackerLocation.deserialize(object.get("loc2"));
                StackerLocation loc3 = StackerLocation.deserialize(object.get("loc3"));

                DataObject obj = new DataObject();

                obj.u1 = u1;
                obj.u2 = u2;
                obj.u3 = u3;

                obj.loc1 = loc1;
                obj.loc2 = loc2;
                obj.loc3 = loc3;

                return obj;
            }
            return null;
        }

        @Override
        public JsonElement serialize(DataObject dataObject, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("u1", u1.toString());
            jsonObject.addProperty("u2", u2.toString());
            jsonObject.addProperty("u3", u3.toString());

            jsonObject.add("loc1", GSON.toJsonTree(loc1));
            jsonObject.add("loc2", GSON.toJsonTree(loc2));
            jsonObject.add("loc3", GSON.toJsonTree(loc3));

            return jsonObject;
        }
    }

}
