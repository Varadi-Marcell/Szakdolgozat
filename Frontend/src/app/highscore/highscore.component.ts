import { Component, OnInit } from '@angular/core';
import { StompService } from "../service/stomp.service";
import { User } from "../model/User";
import { Router } from "@angular/router";

@Component({
  selector: 'app-highscore',
  templateUrl: './highscore.component.html',
  styleUrls: ['./highscore.component.css']
})
export class HighscoreComponent implements OnInit {

  winner: string;
  users : User[];

  constructor(private readonly stompService: StompService,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    this.stompService.resultListObservable.subscribe((msg)=>{
      if (msg){
        this.users = msg;
      } else {
        this.stompService.disconnect();
        this.router.navigateByUrl('/register')
      }
    })
    this.stompService.winnerObservable.subscribe((msg) => {
      if (msg) {
        this.winner = msg;
      } else {
      }
    })
  }

}
