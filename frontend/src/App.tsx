import * as React from 'react';
import './App.css';
import GameContainer from './game/GameContainer';

class App extends React.Component {
  public render() {
    return (
      <div className="App">
          <GameContainer />
        <header className="App-header">
          <h1 className="App-title">Welcome to xyz-bot!</h1>
        </header>
      </div>
    );
  }
}

export default App;
