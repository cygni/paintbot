import * as React from 'react';
import { ChangeEvent } from 'react';
import styled from 'styled-components';

interface Props {
  sliderChange?: (changeNumber: number) => void;
}

interface State {
  currentValue: number;
}

const Container = styled.div`
  width: 100%;
`;

export default class Slider extends React.Component<Props, State> {
  public constructor(props: Props) {
    super(props);
    this.state = {
      currentValue: 0,
    };
    this.sliderChange = this.sliderChange.bind(this);
  }

  public render() {
    return (
      <Container>
        <input
          type={'range'}
          defaultValue={'0'}
          min={'0'}
          max={'100'}
          step={'1'}
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
