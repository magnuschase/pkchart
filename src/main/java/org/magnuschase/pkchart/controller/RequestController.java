package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.magnuschase.pkchart.entity.*;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.model.RequestType;
import org.magnuschase.pkchart.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@Tag(name = "Request API", description = "Card request management")
public class RequestController {
  @Autowired private RequestService requestService;

  @PostMapping("/card/add")
  public ResponseEntity<CardPendingApprovalDTO> requestCardAddition(
      @RequestBody CardRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
    CardPendingApproval cardApprovalRequest =
        requestService.requestCardAddition(request, userDetails);
    return ResponseEntity.ok(CardPendingApprovalDTO.fromEntity(cardApprovalRequest));
  }

  @PostMapping("/set/add")
  public ResponseEntity<CardSetPendingApprovalDTO> requestSetAddition(
      @RequestBody SetRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
    CardSetPendingApproval cardSetApprovalRequest =
        requestService.requestSetAddition(request, userDetails);
    return ResponseEntity.ok(CardSetPendingApprovalDTO.fromEntity(cardSetApprovalRequest));
  }

  @PostMapping("/card/remove")
  public ResponseEntity<CardRemovalRequestDTO> requestCardRemoval(
      @RequestBody CardRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
    CardRemovalRequest cardRemovalRequest = requestService.requestCardRemoval(request, userDetails);
    return ResponseEntity.ok(CardRemovalRequestDTO.fromEntity(cardRemovalRequest));
  }

  @GetMapping("/all")
  public ResponseEntity<AllRequestsResponse> getAllRequests() {
    List<CardPendingApproval> cardAdds = requestService.getAllCardAdditionRequests();
    List<CardRemovalRequest> cardRemoves = requestService.getAllCardRemovalRequests();
    List<CardSetPendingApproval> setAdds = requestService.getAllSetAdditionRequests();
    return ResponseEntity.ok(new AllRequestsResponse(cardAdds, cardRemoves, setAdds));
  }

  @PostMapping("/approve/{requestId}")
  public ResponseEntity<Void> approveRequest(
      @PathVariable Long requestId, @RequestParam RequestType type) {
    requestService.approveRequest(requestId, type);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reject/{requestId}")
  public ResponseEntity<Void> rejectRequest(
      @PathVariable Long requestId, @RequestParam RequestType type) {
    requestService.rejectRequest(requestId, type);
    return ResponseEntity.ok().build();
  }

  public static class AllRequestsResponse {
    public List<CardPendingApprovalDTO> cardAdditions;
    public List<CardRemovalRequestDTO> cardRemovals;
    public List<CardSetPendingApprovalDTO> setAdditions;

    public AllRequestsResponse(
        List<CardPendingApproval> cardAdditions,
        List<CardRemovalRequest> cardRemovals,
        List<CardSetPendingApproval> setAdditions) {
      this.cardAdditions = cardAdditions.stream().map(CardPendingApprovalDTO::fromEntity).toList();
      this.cardRemovals = cardRemovals.stream().map(CardRemovalRequestDTO::fromEntity).toList();
      this.setAdditions = setAdditions.stream().map(CardSetPendingApprovalDTO::fromEntity).toList();
    }
  }
}
