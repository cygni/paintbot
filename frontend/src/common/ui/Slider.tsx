import * as React from 'react';
import { ChangeEvent } from 'react';
import styled from 'styled-components';

interface SliderProps {
  sliderChange?: (changeNumber: number) => void;
  maxValue: number;
  minValue: number;
  defaultValue: number;
  reverse?: boolean;
}

interface InputProps {
  rotateDegrees: number;
}

export default class Slider extends React.Component<SliderProps> {
  public constructor(props: SliderProps) {
    super(props);
    this.sliderChange = this.sliderChange.bind(this);
  }

  public render() {
    const { minValue, maxValue, defaultValue, reverse } = this.props;
    const startValue = defaultValue.toString();
    return (
      <Container>
        <Input
          rotateDegrees={reverse ? 180 : 0}
          type={'range'}
          defaultValue={startValue}
          min={minValue}
          max={maxValue}
          step={'10'}
          onChange={this.sliderChange}
        />
      </Container>
    );
  }

  private sliderChange(changeEvent: ChangeEvent<HTMLInputElement>) {
    const value = parseInt(changeEvent.target.value, 10);
    if (!!this.props.sliderChange && !isNaN(value)) {
      this.props.sliderChange(value);
    }
  }
}

const Container = styled.div`
  width: 100%;
`;

const Input = styled.input`
  -webkit-appearance: none;
  appearance: none;
  width: 100%;
  height: 5px;
  background: #d3d3d3;
  outline: none;
  opacity: 0.7;
  -webkit-transition: 0.2s;
  transform: rotate(${(props: InputProps) => props.rotateDegrees}deg);
`;
