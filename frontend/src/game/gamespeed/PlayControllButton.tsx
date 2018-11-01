import React from 'react';

import { GameControllerColors, StandardColors } from '../../common/Constants';
import { HoverButton } from '../../common/ui/HoverButton';

interface Props {
  playing: boolean;
  onClick: () => void;
}

export const PlayControllButton = ({ playing, onClick }: Props) => {
  const playStatusText = playing ? 'Pause' : 'Play';
  return (
    <HoverButton
      onClick={onClick}
      width={200}
      color={StandardColors.White}
      backgroundColor={GameControllerColors.PlayButton}
    >
      {playStatusText}
    </HoverButton>
  );
};
