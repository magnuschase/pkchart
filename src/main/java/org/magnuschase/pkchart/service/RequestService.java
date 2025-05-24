package org.magnuschase.pkchart.service;

import java.util.List;
import org.magnuschase.pkchart.entity.*;
import org.magnuschase.pkchart.interfaces.CardRequestDTO;
import org.magnuschase.pkchart.interfaces.SetRequestDTO;
import org.magnuschase.pkchart.model.RequestStatus;
import org.magnuschase.pkchart.model.RequestType;
import org.magnuschase.pkchart.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
  @Autowired private CardPendingApprovalRepository cardPendingApprovalRepository;
  @Autowired private CardRemovalRequestRepository cardRemovalRequestRepository;
  @Autowired private CardSetPendingApprovalRepository cardSetPendingApprovalRepository;
  @Autowired private CardRepository cardRepository;
  @Autowired private CardSetRepository cardSetRepository;
  @Autowired private UserRepository userRepository;

  public CardPendingApproval requestCardAddition(CardRequestDTO request, UserDetails userDetails) {
    String email = userDetails.getUsername();
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found for principal"));
    // Use PostgreSQL full text search for set name
    CardSet set =
        cardSetRepository
            .findFirstByNameFullText(request.getSetName())
            .orElseThrow(
                () -> new IllegalArgumentException("Set not found: " + request.getSetName()));
    CardPendingApproval pending = new CardPendingApproval();
    pending.setName(request.getName());
    pending.setRarity(request.getRarity());
    pending.setLanguage(request.getLanguage());
    pending.setNumber(request.getNumber());
    pending.setSet(set);
    pending.setUser(user);
    pending.setStatus(RequestStatus.PENDING);
    return cardPendingApprovalRepository.save(pending);
  }

  public CardRemovalRequest requestCardRemoval(CardRequestDTO request, UserDetails userDetails) {
    String email = userDetails.getUsername();
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found for principal"));
    Card card =
        cardRepository
            .findFirstByNameAndRarityAndSetNameFullText(
                request.getName(), request.getRarity().name(), request.getSetName())
            .orElseThrow(
                () -> new IllegalArgumentException("Card not found: " + request.getName()));
    CardRemovalRequest removal = new CardRemovalRequest();
    removal.setCard(card);
    removal.setUser(user);
    removal.setReason(request.getReason());
    removal.setStatus(RequestStatus.PENDING);
    return cardRemovalRequestRepository.save(removal);
  }

  public CardSetPendingApproval requestSetAddition(SetRequestDTO request, UserDetails userDetails) {
    String email = userDetails.getUsername();
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found for principal"));
    CardSetPendingApproval pending = new CardSetPendingApproval();
    pending.setName(request.getName());
    pending.setSymbol(request.getSymbol());
    pending.setLanguage(request.getLanguage());
    pending.setSize(request.getSize());
    pending.setUser(user);
    pending.setStatus(RequestStatus.PENDING);
    return cardSetPendingApprovalRepository.save(pending);
  }

  public List<CardPendingApproval> getAllCardAdditionRequests() {
    return cardPendingApprovalRepository.findAll();
  }

  public List<CardRemovalRequest> getAllCardRemovalRequests() {
    return cardRemovalRequestRepository.findAll();
  }

  public List<CardSetPendingApproval> getAllSetAdditionRequests() {
    return cardSetPendingApprovalRepository.findAll();
  }

  public void approveRequest(Long requestId, RequestType type) {
    switch (type) {
      case CARD_ADD -> {
        CardPendingApproval req =
            cardPendingApprovalRepository
                .findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        Card card = new Card();
        card.setName(req.getName());
        card.setRarity(req.getRarity());
        card.setSet(req.getSet());
        card.setNumber(req.getNumber());
        cardRepository.save(card);
        req.setStatus(RequestStatus.APPROVED);
        cardPendingApprovalRepository.save(req);
      }
      case CARD_REMOVE -> {
        CardRemovalRequest req =
            cardRemovalRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        Card card = req.getCard();
        if (card != null) {
          cardRepository.delete(card);
        }
        req.setStatus(RequestStatus.APPROVED);
        cardRemovalRequestRepository.save(req);
      }
      case SET_ADD -> {
        CardSetPendingApproval req =
            cardSetPendingApprovalRepository
                .findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        CardSet set = new CardSet();
        set.setName(req.getName());
        set.setSymbol(req.getSymbol());
        set.setLanguage(req.getLanguage());
        set.setSize(req.getSize());
        cardSetRepository.save(set);
        req.setStatus(RequestStatus.APPROVED);
        cardSetPendingApprovalRepository.save(req);
      }
    }
  }

  public void rejectRequest(Long requestId, RequestType type) {
    switch (type) {
      case CARD_ADD -> {
        CardPendingApproval req =
            cardPendingApprovalRepository
                .findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        req.setStatus(RequestStatus.REJECTED);
        cardPendingApprovalRepository.save(req);
      }
      case CARD_REMOVE -> {
        CardRemovalRequest req =
            cardRemovalRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        req.setStatus(RequestStatus.REJECTED);
        cardRemovalRequestRepository.save(req);
      }
      case SET_ADD -> {
        CardSetPendingApproval req =
            cardSetPendingApprovalRepository
                .findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        req.setStatus(RequestStatus.REJECTED);
        cardSetPendingApprovalRepository.save(req);
      }
    }
  }
}
