#Chatbot -  Agile Estimation

## Project Vision
    There is a chatbot connected to OpenAI that acts as a college counselor. The goal is to enhance this chatbot by gamifying the experience for prospective and current students at ACU.  

---


## A Proposal
    We propose a fun and interactive game that helps students determine which major they would best fit into. This game is fun and lighthearted outputting playful answers and taking in silly answers instead of a formal Q&A. 

 *Why do we want to help students define their work?*
    To take off any academic or mental load incoming students have to face in addition to life stressers.  

---

Using the **Fibonacci Scale** for effort estimation:
- **1** – trivial / nearly no effort  
- **2** – small and straightforward  
- **3** – moderate complexity  
- **5** – more complex, may have some unknowns  
- **8+** – complex and/or significant unknowns  

---

### Game Flow and Design
| User Story | Description | Story Points |
|------------|-------------|--------------|
| US1.1 | Define the tone and humor style of the game. | 1 |
| US1.2 | Create a playful game introduction and basic flow. | 2 |
| US1.3 | Create 10–15 funny questions and categorize them. | 2 |

---

### Database Integration
| User Story | Description | Story Points |
|------------|-------------|--------------|
| US2.1 | Create a basic table of questions and answer keywords. | 2 |
| US2.2 | Enable chatbot to fetch questions from the database. | 3 |
| US2.3 | Score user answers and return a matching major. | 5 |

---

### Chatbot Logic Enhancement
| User Story | Description | Story Points |
|------------|-------------|--------------|
| US3.1 | Track session responses. | 2 |
| US3.2 | Generate personalized, funny results. | 2 |

---

### UI/UX (Minimal for MVP)
| User Story | Description | Story Points |
|------------|-------------|--------------|
| US4.1 | Build a clean, simple interface for terminal. | 3 |
| US4.2 | Display questions and answers in chat format. | 2 |

---

###  Testing & Feedback
| User Story | Description | Story Points |
|------------|-------------|--------------|
| US5.1 | Internal test of chatbot question/response flow. | 2 |
| US5.2 | Quick user feedback round with 2 students. | 2 |

---

## 3-Week Sprint Schedule

| Week | Focus | Estimated Points |
|------|-------|------------------|
| Week 1 | Game concept, question design, database setup | 15 |
| Week 2 | Chatbot logic, scoring, basic UI design | 15 |
| Week 3 | Testing, feedback, polish & MVP delivery | 10 |

**Total Estimated Points**: ~40  
**Duration**: 3 Weeks  
**Sprint Type**: Fast-cycle weekly sprints 

---

## Deliverables by End of Week 3  
- [x] Playable game with silly question/answer flow  
- [x] Chatbot integration with OpenAI API  
- [x] Database-driven question selection  
- [x] Result generation based on responses  
- [x] Basic UI for mobile and desktop  
- [x] Internal testing and initial user feedback  

---
