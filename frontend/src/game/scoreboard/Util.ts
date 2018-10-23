import { Character } from '../type';

enum SortOrder {
  ASCENDING,
  DESCENDING,
}

const sortPlayers = (players: Character[], order: SortOrder) => {
  if (order === SortOrder.ASCENDING) {
    return players.sort((player1, player2) => {
      return player1.points - player2.points;
    });
  }

  if (order === SortOrder.DESCENDING) {
    return players.sort((player1, player2) => {
      return player2.points - player1.points;
    });
  }

  return players;
};

export { SortOrder, sortPlayers };
