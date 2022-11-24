import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CanvasComponent } from "../canvas.component";
import { Router } from "@angular/router";
import { StompService } from "../service/stomp.service";
import { User } from "../model/User";
import { Subscription } from "rxjs";

@Component({
  selector: 'app-draw',
  templateUrl: './draw.component.html',
  styleUrls: ['./draw.component.css']
})
export class DrawComponent implements OnInit, OnDestroy {

  @ViewChild('canvasComponent')
  canvasComponent: CanvasComponent;
  clicked = false;

  title = 'DrawMyWord';
  description = 'DrawMyWord';
  hintsArray: string[] = [];
  words: string[] = [];
  user : User = new User();
  disabled = true;
  name: string;
  lengthOfTheWords;
  timeLeft: number = 40;
  interval;
  colors = [
    'black',
    '#FFFFFF',
    '#F9FF33',
    '#ff0000',
    '#E000FF',
    '#5500FF',
    '#00E4FF',
    '#00FF0C'
  ];
  selectedColor: string = this.colors[0];
  users: User[] = [];
  private subscriptions: Array<Subscription> = new Array<Subscription>();

  constructor(private readonly stompService: StompService,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    const user = JSON.parse(sessionStorage.getItem('user'));
    this.stompService.connect(user.name);

    this.subscriptions.push(this.stompService.onConnect.subscribe(() => {

      this.stompService.stompClient.subscribe('/topic/coordinates', (msg) => {
        const coordinates = JSON.parse(msg.body);
        this.selectedColor = coordinates.color;
        this.canvasComponent.drawOnCanvas(coordinates.startX, coordinates.startY,
          coordinates.endX, coordinates.endY);
      });

      this.stompService.stompClient.subscribe('/user/topic/private',  (msg) =>{
          console.log((msg.body));
        this.showHints((msg.body));

      });

      this.stompService.stompClient.subscribe('/user/topic/words', (msg) => {
        try {
          msg.body.split(" ",3).forEach(m => this.pushWords(m));
        } catch (err) {
          console.log(err);
        }
      });

      this.stompService.stompClient.subscribe('/topic/users',  (msg) => {
        console.log(JSON.parse(msg.body).username);

        this.users = JSON.parse(msg.body);

      });

      this.stompService.stompClient.subscribe('/topic/drawpermission',  (msg) => {
        this.showHints(msg.body);
      });

      this.stompService.stompClient.subscribe('/topic/hint-help',  (msg) => {
        this.lengthOfTheWords = Array(Number(msg.body)).fill("_").map((x,i)=>i);
        this.timeLeft = 40;
        this.startTimer();
      });

      this.stompService.stompClient.subscribe('/topic/next-round',  () => {
        this.pauseTimer();
        this.canvasComponent.fillCanvas();
      });

      this.stompService.stompClient.subscribe('/topic/game-over',  (msg) => {
        const game = JSON.parse(msg.body);
        this.stompService.resultList.next(game.users);
        this.stompService.winner.next(game.result);
        this.router.navigateByUrl('/highscore');
      });

      this.stompService.stompClient.subscribe('/topic/disable-button',  (msg) => {
        this.clicked = msg;
      });

      this.stompService.stompClient.subscribe('/topic/hint',  (msg: { body: string; }) => {
        this.showHints(JSON.parse(msg.body).hint);
      });

      this.stompService.stompClient.send('/uni/set-name', {},JSON.stringify({ 'name': user.name}));
    }))

  }

  disconnect() {
    this.stompService.disconnect();
    this.router.navigateByUrl('/register');
  }

  sendHint() {
    this.stompService.stompClient.send(
      '/uni/recieve-hint',
      {},
      JSON.stringify({ 'hint': this.name })
    );
  }

  showHints(message: any) {
    this.hintsArray.push(message);
  }

  pushWords(message: any) {
    this.words.push(message);
  }

  sendCoordinates(data) {
    if (this.stompService.stompClient) {
      this.stompService.stompClient.send(
        '/uni/sendCoordinates',
        {},
        JSON.stringify({
          ...data,
          color: this.selectedColor
        })
      );
    }
  }

  selectWord(word) {
    this.stompService.stompClient.send('/uni/selected-word', {},
      JSON.stringify({ 'word': word }));
    this.words = [];
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  startGame() {
    this.stompService.stompClient.send('/uni/start-game');
    this.stompService.winner.next(null);
  }

  setColor(c) {
    this.selectedColor = c;
  }

  startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
      } else {
        this.timeLeft = 40;
      }
    },1000)
  }

  pauseTimer() {
    clearInterval(this.interval);
  }
}
