package skywolf46.nicknameapi.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import skywolf46.nicknameapi.enumerations.NicknameType;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
public class NicknameData implements ConfigurationSerializable, Serializable, Cloneable {

    private static final String SERIALIZE_PLAYER = "Player";

    private static final String SERIALIZE_NAME_DATA = "NameTypes";

    @Getter
    private final UUID player;

    private final Map<NicknameType, String> names = new HashMap<>();

    @Setter
    private transient List<BiConsumer<NicknameData, NicknameType[]>> updateTrigger = null;

    @Setter
    private transient List<BiConsumer<NicknameData, NicknameType[]>> removeTrigger = null;

    public String getNickname(NicknameType type) {
        return names.get(type);
    }

    public void setNickname(String name, NicknameType... type) {
        for (NicknameType specifiedType : type)
            names.put(specifiedType, name);
        for (BiConsumer<NicknameData, NicknameType[]> trigger : updateTrigger)
            trigger.accept(this, type);
    }

    public void removeNickname(String name, NicknameType... type) {
        for (NicknameType specifiedType : type)
            names.remove(specifiedType, name);
        for (BiConsumer<NicknameData, NicknameType[]> trigger : removeTrigger)
            trigger.accept(this, type);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put(SERIALIZE_PLAYER, player);
        data.put(SERIALIZE_NAME_DATA,
                names.entrySet().stream()
                        .map(x -> new AbstractMap.SimpleEntry<>(x.getKey().toString(), x.getValue()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
        return data;
    }

    @SuppressWarnings("unchecked")
    public static NicknameData deserialize(Map<String, Object> data) {
        NicknameData instance = new NicknameData(UUID.fromString((String) data.get(SERIALIZE_PLAYER)));
        if (data.get(SERIALIZE_NAME_DATA) instanceof ConfigurationSection)
            instance.readFromSection((ConfigurationSection) data.get(SERIALIZE_NAME_DATA));
        else
            for (Map.Entry<String, String> entry : ((Map<String, String>) data.get(SERIALIZE_NAME_DATA)).entrySet())
                instance.names.put(NicknameType.valueOf(entry.getKey()), entry.getValue());
        return instance;
    }

    private void readFromSection(ConfigurationSection sec) {
        for (String x : sec.getKeys(false)) {
            names.put(NicknameType.valueOf(x), sec.getString(x));
        }
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    public NicknameData clone() {
        NicknameData data = new NicknameData(player);
        data.names.putAll(names);
        return data;
    }
}
