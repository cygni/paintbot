package se.cygni.paintbot.socketserver.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.socketserver.game.GameFeatures;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameSettingsMapper {
    GameSettingsMapper INSTANCE = Mappers.getMapper(GameSettingsMapper.class);

    GameSettings gameFeaturesToGameSettings(GameFeatures gameFeatures);

    GameFeatures gameSettingsToGameFeatures(GameSettings gameSettings);
//    public static GameSettings toGameSettings(GameFeatures gameFeatures) {
//        GameSettings gameSettings = new GameSettings();
//        gameFeatures.applyValidation();
//
//        BeanUtils.copyProperties(gameFeatures, gameSettings);
//
//        return gameSettings;
//    }
//
//    public static GameFeatures toGameFeatures(GameSettings gameSettings) {
//        GameFeatures gameFeatures = new GameFeatures();
//        BeanUtils.copyProperties(gameSettings, gameFeatures);
//
//        gameFeatures.applyValidation();
//        return gameFeatures;
//    }
}
