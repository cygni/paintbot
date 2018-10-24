import * as React from 'react';

interface Props {
  startTimeInMinutes: number;
}

interface State {
  timerPresentation: string;
  minutes: number;
  seconds: number;
}

export default class Timer extends React.Component<Props, State> {
  private timerHandle: NodeJS.Timer;
  public constructor(props: Props) {
    super(props);
    const startTime = this.formatTime(this.props.startTimeInMinutes, 0);
    this.state = {
      timerPresentation: startTime,
      minutes: this.props.startTimeInMinutes,
      seconds: 0,
    };
  }

  public render() {
    return <span>{this.state.timerPresentation}</span>;
  }

  public start() {
    this.timerHandle = setInterval(() => this.run(), 1000);
  }

  public stop() {
    clearInterval(this.timerHandle);
  }

  public run() {
    const { seconds, minutes } = this.state;
    if (seconds === 0 && minutes === 0) {
      this.stop();
    } else if (seconds === 0) {
      this.setState(state => {
        return {
          minutes: state.minutes - 1,
          seconds: 59,
          timerPresentation: this.formatTime(state.minutes - 1, 59),
        };
      });
    } else {
      this.setState(state => {
        return {
          ...state,
          seconds: state.seconds - 1,
          timerPresentation: this.formatTime(state.minutes, state.seconds - 1),
        };
      });
    }
  }

  private formatTime(min: number, sec: number): string {
    const minutesString = min < 10 ? `0${min}` : `${min}`;
    const secondsString = sec < 10 ? `0${sec}` : `${sec}`;
    return `${minutesString}:${secondsString}`;
  }
}
