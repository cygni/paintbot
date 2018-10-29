import * as React from 'react';
import { Indent } from '../../common/ui/Indent';
import Slider from '../../common/ui/Slider';
import Config from '../../Config';

interface Props {
  gameSpeedChange?: (value: number) => void;
}

export const GameSpeed = ({ gameSpeedChange }: Props) => (
  <div>
    <Indent num={2}>
      <Slider
        sliderChange={gameSpeedChange}
        minValue={Config.GameSpeedMin}
        maxValue={Config.GameSpeedMax}
        defaultValue={Config.DefaultGameSpeed}
        reverse={true}
      />
    </Indent>
  </div>
);
