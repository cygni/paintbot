import * as React from 'react';
import { Routes } from './Routes';

class App extends React.Component {
  public componentWillMount() {
    document.body.style.margin = '0px';
  }

  public render() {
    return <Routes />;
  }
}

export default App;
