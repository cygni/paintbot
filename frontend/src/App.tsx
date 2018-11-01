import React from 'react';

import Routes from './Routes';

class App extends React.Component {
  componentDidMount() {
    document.body.style.margin = '0px';
  }

  render() {
    return <Routes />;
  }
}

export default App;
