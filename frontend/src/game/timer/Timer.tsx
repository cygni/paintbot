import * as React from 'react';

interface Props {
  durationInSeoncds: number;
  timeInMsPerTick: number;
  worldTick: number;
}

export default class Timer extends React.Component<Props> {
  public render() {
    return <span>{this.getTimeRemaining()}</span>;
  }

  public getTimeRemaining() {
    const { durationInSeoncds, timeInMsPerTick, worldTick } = this.props;
    const totalTimeElapsedInMs = worldTick * timeInMsPerTick;
    const timeLeftInSeconds = Math.floor(
      durationInSeoncds - totalTimeElapsedInMs / 1000,
    );
    const minutes = Math.floor(timeLeftInSeconds / 60);
    const seconds = timeLeftInSeconds % 60;
    return this.formatTime(minutes, seconds);
  }

  private formatTime(min: number, sec: number): string {
    const minutesString = min < 10 ? `0${min}` : `${min}`;
    const secondsString = sec < 10 ? `0${sec}` : `${sec}`;
    return `${minutesString}:${secondsString}`;
  }
}
